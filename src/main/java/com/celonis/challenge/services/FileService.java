package com.celonis.challenge.services;

import com.celonis.challenge.model.dto.ProjectGenerationTaskDTO;
import com.celonis.challenge.model.dto.TaskDTO;
import java.io.IOException;
import java.net.URL;
import org.springframework.core.io.FileSystemResource;

public interface FileService {

  void storeResult(TaskDTO task, URL url) throws IOException;

  FileSystemResource getTaskResult(ProjectGenerationTaskDTO task);
}
