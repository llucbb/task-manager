package com.celonis.challenge.services.impl;

import static com.celonis.challenge.config.AppConstants.COUNTER_TASK;

import com.celonis.challenge.concurrent.CallableTask;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@RequiredArgsConstructor
@Service(COUNTER_TASK)
public class CounterTaskExecutorService implements TaskExecutorService {

  @Value("${application.counter-delay}")
  private long counterDelay;

  private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
  private final CounterTaskRepository counterTaskRepository;
  private final TaskMapper taskMapper;

  // Map with the executing counter tasks, allowing task cancellation
  private Map<String, ListenableFuture<CounterTaskDTO>> submittedTasks;

  @PostConstruct
  public void init() {
    submittedTasks = new HashMap<>();
  }

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

    // Submit the task
    ListenableFuture<CounterTaskDTO> future =
        threadPoolTaskExecutor.submitListenable(new CallableTask(counterTask, counterDelay));

    // Add call back in order to manage completed tasks
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

    // Add the task to the list of tasks in execution
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
    // In order to cancel the task has to be in execution, not cancelled and not completed (rare
    // cases)
    if (future != null && !future.isCancelled() && !future.isDone()) {
      // Cancel task
      future.cancel(true);
      // Remove task from executing task list
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
        String.format("Counter task %s does not have any available result", task.getName()));
  }
}
