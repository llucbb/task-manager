package com.celonis.challenge.services.impl;

import com.celonis.challenge.dto.ProjectGenerationTaskDTO;
import com.celonis.challenge.dto.TaskDTO;
import com.celonis.challenge.dto.TaskResultDTO;
import com.celonis.challenge.dto.TaskStatusDTO;
import com.celonis.challenge.exceptions.InternalException;
import com.celonis.challenge.services.FileService;
import com.celonis.challenge.services.TaskExecutorService;
import java.io.IOException;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("PROJECT_GENERATION")
public class ProjectGenerationTaskExecutorService implements TaskExecutorService {

  private final FileService fileService;

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
  public void cancelTask(TaskDTO task) {}

  @Override
  public TaskStatusDTO getTaskStatus(TaskDTO task) {
    return null;
  }

  @Override
  public TaskResultDTO<FileSystemResource> getTaskResult(TaskDTO task) {
    return new TaskResultDTO<>(fileService.getTaskResult((ProjectGenerationTaskDTO) task));
  }
}