package com.celonis.challenge.services;

import com.celonis.challenge.model.dto.TaskDTO;
import com.celonis.challenge.model.dto.TaskResultDTO;
import com.celonis.challenge.model.dto.TaskStatusDTO;
import java.util.List;

public interface TaskService {

  List<TaskDTO> listTasks();

  TaskDTO createTask(TaskDTO task);

  TaskDTO getTask(String taskId);

  TaskDTO update(String taskId, TaskDTO task);

  void delete(String taskId);

  void executeTask(String taskId);

  TaskStatusDTO getTaskStatus(String taskId);

  TaskResultDTO<?> getTaskResult(String taskId);

  void cancel(String taskId);

  void deleteNonExecutedTasks();
}
