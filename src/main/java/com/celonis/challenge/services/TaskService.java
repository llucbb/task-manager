package com.celonis.challenge.services;

import com.celonis.challenge.dto.TaskDTO;
import com.celonis.challenge.dto.TaskResultDTO;
import java.util.List;

public interface TaskService {

  List<TaskDTO> listTasks();

  TaskDTO createTask(TaskDTO task);

  TaskDTO getTask(String taskId);

  TaskDTO update(String taskId, TaskDTO task);

  void delete(String taskId);

  void executeTask(String taskId);

  TaskResultDTO<?> getTaskResult(String taskId);
}
