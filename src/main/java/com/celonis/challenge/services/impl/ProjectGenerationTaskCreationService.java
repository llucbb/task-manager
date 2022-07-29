package com.celonis.challenge.services.impl;

import com.celonis.challenge.dto.ProjectGenerationTaskDTO;
import com.celonis.challenge.dto.TaskDTO;
import com.celonis.challenge.mapper.TaskMapper;
import com.celonis.challenge.repositories.ProjectGenerationTaskRepository;
import com.celonis.challenge.services.TaskCreationService;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("PROJECT_GENERATION_CREATE")
public class ProjectGenerationTaskCreationService implements TaskCreationService {

  private final ProjectGenerationTaskRepository repository;
  private final TaskMapper mapper;

  @Override
  public ProjectGenerationTaskDTO createTask(TaskDTO task) {
    ProjectGenerationTaskDTO projectGenerationTask = (ProjectGenerationTaskDTO) task;
    projectGenerationTask.setId(null);
    projectGenerationTask.setCreationDate(new Date());
    return mapper.map(repository.save(mapper.map(projectGenerationTask)));
  }
}
