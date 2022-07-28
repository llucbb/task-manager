package com.celonis.challenge.services;

import java.io.IOException;
import java.net.URL;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

public interface FileService {

  ResponseEntity<FileSystemResource> getTaskResult(String taskId);

  void storeResult(String taskId, URL url) throws IOException;
}
