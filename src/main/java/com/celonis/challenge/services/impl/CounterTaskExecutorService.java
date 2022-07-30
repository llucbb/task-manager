package com.celonis.challenge.services.impl;

import com.celonis.challenge.concurrent.CallableTask;
import com.celonis.challenge.config.AppConstants;
import com.celonis.challenge.exceptions.TaskException;
import com.celonis.challenge.mapper.TaskMapper;
import com.celonis.challenge.model.dto.CounterTaskDTO;
import com.celonis.challenge.model.dto.TaskDTO;
import com.celonis.challenge.model.dto.TaskResultDTO;
import com.celonis.challenge.model.dto.TaskStatusDTO;
import com.celonis.challenge.repositories.CounterTaskRepository;
import com.celonis.challenge.services.TaskExecutorService;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@RequiredArgsConstructor
@Service(AppConstants.COUNTER_TASK)
public class CounterTaskExecutorService implements TaskExecutorService {

  private static final int COUNTER_DELAY_MS = 1000;

  @Getter private Map<String, ListenableFuture<CounterTaskDTO>> submittedTasks;

  @PostConstruct
  public void init() {
    submittedTasks = new HashMap<>();
  }

  private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
  private final CounterTaskRepository counterTaskRepository;
  private final TaskMapper taskMapper;

  @Override
  public boolean isCompleted(TaskDTO task) {
    CounterTaskDTO counterTask = (CounterTaskDTO) task;
    return counterTask.getX() >= counterTask.getY();
  }

  @Override
  @Transactional
  public void executeTask(TaskDTO task) {
    CounterTaskDTO counterTask = (CounterTaskDTO) task;
    if (isCompleted(counterTask)) {
      throw new TaskException("Task has been completed");
    }

    CallableTask callableTask = new CallableTask(counterTask, COUNTER_DELAY_MS);
    ListenableFuture<CounterTaskDTO> future = threadPoolTaskExecutor.submitListenable(callableTask);

    future.addCallback(
        new ListenableFutureCallback<>() {

          @Override
          public void onFailure(Throwable ex) {
            throw new TaskException(ex);
          }

          @Override
          public void onSuccess(CounterTaskDTO counterTask) {
            submittedTasks.remove(counterTask.getId());
            counterTaskRepository.save(taskMapper.map(counterTask));
          }
        });

    submittedTasks.put(task.getId(), future);
  }

  @Override
  @Transactional
  public void cancelTask(TaskDTO task) {
    CounterTaskDTO counterTask = (CounterTaskDTO) task;
    if (isCompleted(counterTask)) {
      throw new TaskException("Task has been completed and cannot be cancelled");
    }
    String taskId = counterTask.getId();
    ListenableFuture<CounterTaskDTO> future = submittedTasks.get(taskId);
    if (future != null && !future.isCancelled() && !future.isDone()) {
      future.cancel(true);
      submittedTasks.remove(taskId);
      log.info("Cancelled task {}", task.getName());
    }
  }

  @Override
  public TaskStatusDTO getTaskStatus(TaskDTO task) {
    CounterTaskDTO counterTask = (CounterTaskDTO) task;
    ListenableFuture<CounterTaskDTO> future = submittedTasks.get(counterTask.getId());
    if (future != null) {
      return new TaskStatusDTO(task, true, future.isDone());
    } else if (isCompleted(counterTask)) {
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
