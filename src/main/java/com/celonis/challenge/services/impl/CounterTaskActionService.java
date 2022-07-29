package com.celonis.challenge.services.impl;

import com.celonis.challenge.dto.CounterTaskDTO;
import com.celonis.challenge.dto.TaskDTO;
import com.celonis.challenge.dto.TaskResultDTO;
import com.celonis.challenge.mapper.TaskMapper;
import com.celonis.challenge.repositories.CounterTaskRepository;
import com.celonis.challenge.schedule.RunnableTask;
import com.celonis.challenge.services.TaskActionService;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("COUNTER")
public class CounterTaskActionService implements TaskActionService {

  private final CounterTaskRepository repository;
  private final ThreadPoolTaskScheduler taskScheduler;
  private final TaskMapper mapper;

  @Override
  public TaskDTO createTask(TaskDTO task) {
    CounterTaskDTO counterTask = (CounterTaskDTO) task;
    counterTask.setId(null);
    counterTask.setCreationDate(new Date());
    return mapper.map(repository.save(mapper.map(counterTask)));
  }

  @Override
  public TaskDTO update(TaskDTO task) {
    CounterTaskDTO counterTask = (CounterTaskDTO) task;
    return mapper.map(repository.save(mapper.map(counterTask)));
  }

  @Override
  public void executeTask(TaskDTO task) {
    CounterTaskDTO counterTask = (CounterTaskDTO) task;
    taskScheduler.scheduleAtFixedRate(new RunnableTask(counterTask), 1000);
  }

  @Override
  public TaskResultDTO<?> getTaskResult(TaskDTO task) {
    return null;
  }
}
