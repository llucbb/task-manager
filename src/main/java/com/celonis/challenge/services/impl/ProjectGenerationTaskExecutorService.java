package com.celonis.challenge.services.impl;

import static com.celonis.challenge.config.AppConstants.PROJECT_GENERATION_TASK;

import com.celonis.challenge.exceptions.InternalException;
import com.celonis.challenge.exceptions.TaskException;
import com.celonis.challenge.model.dto.ProjectGenerationTaskDTO;
import com.celonis.challenge.model.dto.TaskDTO;
import com.celonis.challenge.model.dto.TaskResultDTO;
import com.celonis.challenge.model.dto.TaskStatusDTO;
import com.celonis.challenge.services.FileService;
import com.celonis.challenge.services.TaskExecutorService;
import java.io.IOException;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service(PROJECT_GENERATION_TASK)
@RequiredArgsConstructor
public class ProjectGenerationTaskExecutorService implements TaskExecutorService {

  @Value("${application.project-generation-file}")
  private String projectGenerationFile;

  private final FileService fileService;

  @Override
  public boolean isCompleted(TaskDTO task) {
    ProjectGenerationTaskDTO projectGenerationTask = (ProjectGenerationTaskDTO) task;
    return StringUtils.hasText(projectGenerationTask.getStorageLocation());
  }

  @Override
  @Transactional
  public void executeTask(TaskDTO task) {
    ProjectGenerationTaskDTO projectGenerationTask = (ProjectGenerationTaskDTO) task;
    if (isCompleted(projectGenerationTask)) {
      throw new TaskException("Task has already been executed");
    }
    try {
      fileService.storeResult(task, validateInputAndGet());
    } catch (IOException e) {
      throw new InternalException(e);
    }
  }

  private URL validateInputAndGet() {
    URL url = TaskServiceImpl.class.getResource("/" + projectGenerationFile);
    if (url == null) {
      throw new InternalException("Zip file not found");
    }
    return url;
  }

  @Override
  public void cancelTask(TaskDTO task) {
    throw new TaskException("Task can't be cancelled");
  }

  @Override
  public TaskStatusDTO getTaskStatus(TaskDTO task) {
    ProjectGenerationTaskDTO projectGenerationTask = (ProjectGenerationTaskDTO) task;
    return new TaskStatusDTO(
        task, isCompleted(projectGenerationTask), isCompleted(projectGenerationTask));
  }

  @Override
  public TaskResultDTO<FileSystemResource> getTaskResult(TaskDTO task) {
    return new TaskResultDTO<>(fileService.getTaskResult((ProjectGenerationTaskDTO) task));
  }
}
