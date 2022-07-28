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
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

  private final TaskMapper taskMapper;
  private final FileService fileService;
  private final ProjectGenerationTaskRepository projectGenerationTaskRepository;

  @Override
  @Transactional(readOnly = true)
  public List<ProjectGenerationTaskDTO> listTasks() {
    return taskMapper.map(projectGenerationTaskRepository.findAll());
  }

  @Override
  @Transactional
  public ProjectGenerationTaskDTO createTask(ProjectGenerationTaskDTO projectGenerationTask) {
    projectGenerationTask.setId(null);
    projectGenerationTask.setCreationDate(new Date());
    return taskMapper.map(
        projectGenerationTaskRepository.save(taskMapper.map(projectGenerationTask)));
  }

  @Override
  @Transactional(readOnly = true)
  public ProjectGenerationTaskDTO getTask(String taskId) {
    Optional<ProjectGenerationTask> projectGenerationTask =
        projectGenerationTaskRepository.findById(taskId);

    return projectGenerationTask.map(taskMapper::map).orElseThrow(NotFoundException::new);
  }

  @Override
  @Transactional
  public ProjectGenerationTaskDTO update(
      String taskId, ProjectGenerationTaskDTO projectGenerationTask) {
    ProjectGenerationTaskDTO existing = getTask(taskId);
    existing.setCreationDate(projectGenerationTask.getCreationDate());
    existing.setName(projectGenerationTask.getName());
    return taskMapper.map(projectGenerationTaskRepository.save(taskMapper.map(existing)));
  }

  @Override
  @Transactional
  public void delete(String taskId) {
    try {
      projectGenerationTaskRepository.deleteById(taskId);
    } catch (EmptyResultDataAccessException e) {
      throw new NotFoundException(e);
    }
  }

  @Override
  @Transactional
  public void executeTask(String taskId) {
    ProjectGenerationTaskDTO projectGenerationTask = getTask(taskId);
    URL url = TaskServiceImpl.class.getResource("/challenge.zip");
    if (url == null) {
      throw new InternalException("Zip file not found");
    }
    try {
      fileService.storeResult(projectGenerationTask, url);
    } catch (IOException e) {
      throw new InternalException(e);
    }
  }

  @Override
  @Transactional
  public ResponseEntity<FileSystemResource> getTaskResult(String taskId) {
    ProjectGenerationTaskDTO projectGenerationTask = getTask(taskId);
    return fileService.getTaskResult(projectGenerationTask);
  }
}
