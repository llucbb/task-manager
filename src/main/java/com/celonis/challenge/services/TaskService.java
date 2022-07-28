package com.celonis.challenge.services;

import com.celonis.challenge.dto.ProjectGenerationTaskDTO;
import java.util.List;

public interface TaskService {

  List<ProjectGenerationTaskDTO> listTasks();

  ProjectGenerationTaskDTO createTask(ProjectGenerationTaskDTO projectGenerationTask);

  ProjectGenerationTaskDTO getTask(String taskId);

  ProjectGenerationTaskDTO update(String taskId, ProjectGenerationTaskDTO projectGenerationTask);

  void delete(String taskId);

  void executeTask(String taskId);
}
