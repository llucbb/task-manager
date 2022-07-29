package com.celonis.challenge.services.impl;

import com.celonis.challenge.dto.CounterTaskDTO;
import com.celonis.challenge.dto.TaskDTO;
import com.celonis.challenge.dto.TaskResultDTO;
import com.celonis.challenge.schedule.RunnableTask;
import com.celonis.challenge.services.TaskExecutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("COUNTER_EXECUTE")
public class CounterTaskExecutorService implements TaskExecutorService {

  private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

  @Override
  public void executeTask(TaskDTO task) {
    CounterTaskDTO counterTask = (CounterTaskDTO) task;
    threadPoolTaskScheduler.scheduleAtFixedRate(new RunnableTask(counterTask), 1000);
  }

  @Override
  public TaskResultDTO<?> getTaskResult(TaskDTO task) {
    return null;
  }
}
