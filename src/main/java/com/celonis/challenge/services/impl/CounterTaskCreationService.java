package com.celonis.challenge.services.impl;

import com.celonis.challenge.dto.CounterTaskDTO;
import com.celonis.challenge.dto.TaskDTO;
import com.celonis.challenge.mapper.TaskMapper;
import com.celonis.challenge.repositories.CounterTaskRepository;
import com.celonis.challenge.services.TaskCreationService;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("COUNTER_CREATE")
public class CounterTaskCreationService implements TaskCreationService {

  private final CounterTaskRepository repository;
  private final TaskMapper taskMapper;

  @Override
  public CounterTaskDTO createTask(TaskDTO task) {
    CounterTaskDTO counterTask = (CounterTaskDTO) task;
    counterTask.setId(null);
    counterTask.setCreationDate(new Date());
    return taskMapper.map(repository.save(taskMapper.map(counterTask)));
  }
}
