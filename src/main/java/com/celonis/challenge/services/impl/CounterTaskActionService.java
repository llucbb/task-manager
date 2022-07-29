package com.celonis.challenge.services.impl;

import com.celonis.challenge.dto.CounterTaskDTO;
import com.celonis.challenge.dto.TaskDTO;
import com.celonis.challenge.dto.TaskResultDTO;
import com.celonis.challenge.schedule.RunnableTask;
import com.celonis.challenge.services.TaskActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("COUNTER")
public class CounterTaskActionService implements TaskActionService {

  private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

  @Override
  public void executeTask(TaskDTO task) {
    CounterTaskDTO counterTask = (CounterTaskDTO) task;
    threadPoolTaskExecutor.submit(new RunnableTask(counterTask));
  }

  @Override
  public TaskResultDTO<?> getTaskResult(TaskDTO task) {
    return null;
  }
}
