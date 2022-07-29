package com.celonis.challenge.services;

import com.celonis.challenge.dto.TaskDTO;
import com.celonis.challenge.dto.TaskResultDTO;

public interface TaskActionService {

  void executeTask(TaskDTO task);

  TaskResultDTO<?> getTaskResult(TaskDTO task);
}
