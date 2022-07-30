package com.celonis.challenge.services;

import com.celonis.challenge.model.dto.TaskDTO;
import com.celonis.challenge.model.dto.TaskResultDTO;
import com.celonis.challenge.model.dto.TaskStatusDTO;

public interface TaskExecutorService {

  boolean isCompleted(TaskDTO task);

  void executeTask(TaskDTO task);

  void cancelTask(TaskDTO task);

  TaskStatusDTO getTaskStatus(TaskDTO task);

  TaskResultDTO<?> getTaskResult(TaskDTO task);
}
