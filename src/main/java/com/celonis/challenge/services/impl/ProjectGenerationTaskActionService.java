package com.celonis.challenge.services.impl;

import com.celonis.challenge.dto.ProjectGenerationTaskDTO;
import com.celonis.challenge.dto.TaskDTO;
import com.celonis.challenge.dto.TaskResultDTO;
import com.celonis.challenge.exceptions.InternalException;
import com.celonis.challenge.mapper.TaskMapper;
import com.celonis.challenge.repositories.ProjectGenerationTaskRepository;
import com.celonis.challenge.services.FileService;
import com.celonis.challenge.services.TaskActionService;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("PROJECT_GENERATION")
public class ProjectGenerationTaskActionService implements TaskActionService {

  private final FileService fileService;
  private final ProjectGenerationTaskRepository repository;
  private final TaskMapper mapper;

  @Override
  public ProjectGenerationTaskDTO createTask(TaskDTO task) {
    ProjectGenerationTaskDTO projectGenerationTask = (ProjectGenerationTaskDTO) task;
    projectGenerationTask.setId(null);
    projectGenerationTask.setCreationDate(new Date());
    return mapper.map(repository.save(mapper.map(projectGenerationTask)));
  }

  @Override
  public TaskDTO update(TaskDTO task) {
    ProjectGenerationTaskDTO projectGenerationTask = (ProjectGenerationTaskDTO) task;
    return mapper.map(repository.save(mapper.map(projectGenerationTask)));
  }

  @Override
  public void executeTask(TaskDTO task) {
    URL url = TaskServiceImpl.class.getResource("/challenge.zip");
    if (url == null) {
      throw new InternalException("Zip file not found");
    }
    try {
      fileService.storeResult(task, url);
    } catch (IOException e) {
      throw new InternalException(e);
    }
  }

  @Override
  public TaskResultDTO<FileSystemResource> getTaskResult(TaskDTO task) {
    return new TaskResultDTO<>(fileService.getTaskResult((ProjectGenerationTaskDTO) task));
  }
}
