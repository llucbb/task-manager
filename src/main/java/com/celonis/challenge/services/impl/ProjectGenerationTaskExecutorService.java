package com.celonis.challenge.services.impl;

import com.celonis.challenge.dto.ProjectGenerationTaskDTO;
import com.celonis.challenge.dto.TaskDTO;
import com.celonis.challenge.dto.TaskResultDTO;
import com.celonis.challenge.dto.TaskStatusDTO;
import com.celonis.challenge.exceptions.InternalException;
import com.celonis.challenge.exceptions.TaskException;
import com.celonis.challenge.services.FileService;
import com.celonis.challenge.services.TaskExecutorService;
import java.io.IOException;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service("ProjectGenerationTask")
public class ProjectGenerationTaskExecutorService implements TaskExecutorService {

  private final FileService fileService;

  @Override
  @Transactional
  public void executeTask(TaskDTO task) {
    validateExecution((ProjectGenerationTaskDTO) task);
    URL url = validateInput();
    try {
      fileService.storeResult(task, url);
    } catch (IOException e) {
      throw new InternalException(e);
    }
  }

  private void validateExecution(ProjectGenerationTaskDTO projectGenerationTask) {
    if (!projectGenerationTask.done()) {
      throw new TaskException("Task has already been executed");
    }
  }

  private URL validateInput() {
    URL url = TaskServiceImpl.class.getResource("/challenge.zip");
    if (url == null) {
      throw new InternalException("Zip file not found");
    }
    return url;
  }

  @Override
  public void cancelTask(TaskDTO task) {
    throw new TaskException("Task cannot be cancelled");
  }

  @Override
  public TaskStatusDTO getTaskStatus(TaskDTO task) {
    ProjectGenerationTaskDTO projectGenerationTask = (ProjectGenerationTaskDTO) task;
    if (projectGenerationTask.done()) {
      return new TaskStatusDTO(task, true, true);
    } else {
      return new TaskStatusDTO(task, false, false);
    }
  }

  @Override
  public TaskResultDTO<FileSystemResource> getTaskResult(TaskDTO task) {
    return new TaskResultDTO<>(fileService.getTaskResult((ProjectGenerationTaskDTO) task));
  }
}
