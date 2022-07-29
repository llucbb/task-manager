package com.celonis.challenge.services.impl;

import com.celonis.challenge.concurrent.CallableTask;
import com.celonis.challenge.dto.CounterTaskDTO;
import com.celonis.challenge.dto.TaskDTO;
import com.celonis.challenge.dto.TaskResultDTO;
import com.celonis.challenge.dto.TaskStatusDTO;
import com.celonis.challenge.exceptions.TaskException;
import com.celonis.challenge.mapper.TaskMapper;
import com.celonis.challenge.repositories.CounterTaskRepository;
import com.celonis.challenge.services.TaskExecutorService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@RequiredArgsConstructor
@Service("CounterTask")
public class CounterTaskExecutorService implements TaskExecutorService {

  private Map<String, ListenableFuture<CounterTaskDTO>> runningTasks = new HashMap<>();
  private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
  private final CounterTaskRepository counterTaskRepository;
  private final TaskMapper taskMapper;

  @Override
  @Transactional
  public void executeTask(TaskDTO task) {
    CounterTaskDTO counterTask = (CounterTaskDTO) task;
    validateExecution(counterTask);
    ListenableFuture<CounterTaskDTO> future =
        threadPoolTaskExecutor.submitListenable(new CallableTask(counterTask, 1000));
    future.addCallback(
        new ListenableFutureCallback<>() {

          @Override
          public void onFailure(Throwable ex) {
            throw new TaskException(ex);
          }

          @Override
          public void onSuccess(CounterTaskDTO counterTask) {
            runningTasks.remove(counterTask.getId());
            counterTaskRepository.save(taskMapper.map(counterTask));
          }
        });
    runningTasks.put(task.getId(), future);
  }

  private void validateExecution(CounterTaskDTO counterTask) {
    if (counterTask.done()) {
      throw new TaskException("Task has been completed");
    }
  }

  @Override
  @Transactional
  public void cancelTask(TaskDTO task) {
    CounterTaskDTO counterTask = (CounterTaskDTO) task;
    if (counterTask.done()) {
      throw new TaskException("Task has been completed and cannot be cancelled");
    }
    String taskId = counterTask.getId();
    ListenableFuture<CounterTaskDTO> future = runningTasks.get(taskId);
    if (future != null && !future.isCancelled() && !future.isDone()) {
      future.cancel(true);
      runningTasks.remove(taskId);
      log.info("Cancelled task {}", task.getName());
    }
  }

  @Override
  public TaskStatusDTO getTaskStatus(TaskDTO task) {
    CounterTaskDTO counterTask = (CounterTaskDTO) task;
    ListenableFuture<CounterTaskDTO> future = runningTasks.get(counterTask.getId());
    if (future != null) {
      return new TaskStatusDTO(task, true, future.isDone());
    } else if (counterTask.done()) {
      return new TaskStatusDTO(task, true, true);
    } else {
      return new TaskStatusDTO(task, false, false);
    }
  }

  @Override
  public TaskResultDTO<?> getTaskResult(TaskDTO task) {
    throw new TaskException(
        String.format("Task %s does not have any available result", task.getName()));
  }
}
