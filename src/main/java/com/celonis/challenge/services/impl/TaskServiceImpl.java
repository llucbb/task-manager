package com.celonis.challenge.services.impl;

import com.celonis.challenge.dto.ProjectGenerationTaskDTO;
import com.celonis.challenge.exceptions.InternalException;
import com.celonis.challenge.exceptions.NotFoundException;
import com.celonis.challenge.mapper.TaskMapper;
import com.celonis.challenge.model.ProjectGenerationTask;
import com.celonis.challenge.repositories.ProjectGenerationTaskRepository;
import com.celonis.challenge.services.FileService;
import com.celonis.challenge.services.TaskService;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService, InitializingBean {

  private final ProjectGenerationTaskRepository projectGenerationTaskRepository;
  private final TaskMapper taskMapper;
  private final ApplicationContext context;

  private FileService fileService;

  @Override
  public void afterPropertiesSet() {
    fileService = context.getBean(FileService.class);
  }

  @Override
  public List<ProjectGenerationTaskDTO> listTasks() {
    return taskMapper.map(projectGenerationTaskRepository.findAll());
  }

  @Override
  public ProjectGenerationTaskDTO createTask(ProjectGenerationTaskDTO projectGenerationTask) {
    projectGenerationTask.setId(null);
    projectGenerationTask.setCreationDate(new Date());
    return taskMapper.map(
        projectGenerationTaskRepository.save(taskMapper.map(projectGenerationTask)));
  }

  @Override
  public ProjectGenerationTaskDTO getTask(String taskId) {
    Optional<ProjectGenerationTask> projectGenerationTask =
        projectGenerationTaskRepository.findById(taskId);

    return projectGenerationTask.map(taskMapper::map).orElseThrow(NotFoundException::new);
  }

  @Override
  public ProjectGenerationTaskDTO update(
      String taskId, ProjectGenerationTaskDTO projectGenerationTask) {
    ProjectGenerationTaskDTO existing = getTask(taskId);
    existing.setCreationDate(projectGenerationTask.getCreationDate());
    existing.setName(projectGenerationTask.getName());
    return taskMapper.map(projectGenerationTaskRepository.save(taskMapper.map(existing)));
  }

  @Override
  public void delete(String taskId) {
    try {
      projectGenerationTaskRepository.deleteById(taskId);
    } catch (EmptyResultDataAccessException e) {
      throw new NotFoundException(e);
    }
  }

  @Override
  public void executeTask(String taskId) {
    URL url = TaskServiceImpl.class.getResource("/challenge.zip");
    if (url == null) {
      throw new InternalException("Zip file not found");
    }
    try {
      fileService.storeResult(taskId, url);
    } catch (IOException e) {
      throw new InternalException(e);
    }
  }
}
