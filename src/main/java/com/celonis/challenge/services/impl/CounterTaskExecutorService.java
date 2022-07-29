package com.celonis.challenge.services.impl;

import com.celonis.challenge.concurrent.CallableTask;
import com.celonis.challenge.dto.CounterTaskDTO;
import com.celonis.challenge.dto.TaskDTO;
import com.celonis.challenge.dto.TaskResultDTO;
import com.celonis.challenge.dto.TaskStatusDTO;
import com.celonis.challenge.exceptions.InternalException;
import com.celonis.challenge.exceptions.NotFoundException;
import com.celonis.challenge.exceptions.TaskFailureException;
import com.celonis.challenge.repositories.CounterTaskRepository;
import com.celonis.challenge.services.TaskExecutorService;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@RequiredArgsConstructor
@Service("COUNTER")
public class CounterTaskExecutorService implements TaskExecutorService {

  private Map<String, ListenableFuture<CounterTaskDTO>> runningTasks = new HashMap<>();
  private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
  private final CounterTaskRepository counterTaskRepository;

  @Override
  public void executeTask(TaskDTO task) {
    CounterTaskDTO counterTask = (CounterTaskDTO) task;
    ListenableFuture<CounterTaskDTO> future =
        threadPoolTaskExecutor.submitListenable(new CallableTask(counterTask, 1000));
    future.addCallback(
        new ListenableFutureCallback<>() {

          @Override
          public void onFailure(Throwable ex) {
            throw new TaskFailureException(ex);
          }

          @Override
          public void onSuccess(CounterTaskDTO counterTask) {
            String taskId = removeTask(counterTask.getId());
            log.info("Success executing task, id:" + taskId);
          }

          private String removeTask(String taskId) {
            runningTasks.remove(taskId);
            counterTaskRepository.deleteById(taskId);
            return taskId;
          }
        });
    runningTasks.put(task.getId(), future);
  }

  @Override
  public void cancelTask(TaskDTO task) {
    String taskId = task.getId();
    ListenableFuture<CounterTaskDTO> future = runningTasks.get(taskId);
    if (future != null && !future.isCancelled() && !future.isDone()) {
      future.cancel(true);
      runningTasks.remove(taskId);
      counterTaskRepository.deleteById(taskId);
      log.info("Cancelled task, id:" + taskId);
    }
  }

  @Override
  public TaskStatusDTO getTaskStatus(TaskDTO task) {
    ListenableFuture<?> future = runningTasks.get(task.getId());
    if (future != null) {
      return new TaskStatusDTO(true, future.isDone(), future.isCancelled());
    } else {
      return new TaskStatusDTO(false, false, false);
    }
  }

  @Override
  public TaskResultDTO<?> getTaskResult(TaskDTO task) {
    try {
      Future<?> future = runningTasks.get(task.getId());
      if (future != null) {
        if (future.isDone()) {
          return new TaskResultDTO<>(future.get());
        } else if (future.isCancelled()) {
          runningTasks.remove(task.getId());
          throw new TaskFailureException("Task has been cancelled, id: " + task.getId());
        } else {
          throw new TaskFailureException("Task not been completed yet, id: " + task.getId());
        }
      } else {
        throw new NotFoundException("Task not found, id: " + task.getId());
      }
    } catch (InterruptedException | ExecutionException e) {
      throw new InternalException(e);
    }
  }
}
