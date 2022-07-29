package com.celonis.challenge.services;

import com.celonis.challenge.dto.TaskDTO;
import com.celonis.challenge.dto.TaskResultDTO;
import com.celonis.challenge.dto.TaskStatusDTO;

public interface TaskExecutorService {

  void executeTask(TaskDTO task);

  void cancelTask(TaskDTO task);

  TaskStatusDTO getTaskStatus(TaskDTO task);

  TaskResultDTO<?> getTaskResult(TaskDTO task);
}
