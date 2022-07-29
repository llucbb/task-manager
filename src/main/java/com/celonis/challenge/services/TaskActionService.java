package com.celonis.challenge.services;

import com.celonis.challenge.dto.TaskDTO;
import com.celonis.challenge.dto.TaskResultDTO;

public interface TaskActionService {

  TaskDTO createTask(TaskDTO task);

  void executeTask(TaskDTO task);

  TaskDTO update(TaskDTO task);

  TaskResultDTO<?> getTaskResult(TaskDTO task);
}
