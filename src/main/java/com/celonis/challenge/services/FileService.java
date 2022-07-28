package com.celonis.challenge.services;

import com.celonis.challenge.dto.ProjectGenerationTaskDTO;
import java.io.IOException;
import java.net.URL;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

public interface FileService {

  ResponseEntity<FileSystemResource> getTaskResult(ProjectGenerationTaskDTO projectGenerationTask);

  void storeResult(ProjectGenerationTaskDTO projectGenerationTask, URL url) throws IOException;
}
