package com.celonis.challenge.services.impl;

import com.celonis.challenge.dto.ProjectGenerationTaskDTO;
import com.celonis.challenge.exceptions.InternalException;
import com.celonis.challenge.mapper.TaskMapper;
import com.celonis.challenge.repositories.ProjectGenerationTaskRepository;
import com.celonis.challenge.services.FileService;
import com.celonis.challenge.services.TaskService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

  private final ProjectGenerationTaskRepository projectGenerationTaskRepository;
  private final TaskMapper taskMapper;
  private final TaskService taskService;

  @Override
  public ResponseEntity<FileSystemResource> getTaskResult(String taskId) {
    ProjectGenerationTaskDTO projectGenerationTask = taskService.getTask(taskId);
    File inputFile = new File(projectGenerationTask.getStorageLocation());

    if (!inputFile.exists()) {
      throw new InternalException("File not generated yet");
    }

    HttpHeaders respHeaders = new HttpHeaders();
    respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    respHeaders.setContentDispositionFormData("attachment", "challenge.zip");

    return new ResponseEntity<>(new FileSystemResource(inputFile), respHeaders, HttpStatus.OK);
  }

  @Override
  public void storeResult(String taskId, URL url) throws IOException {
    ProjectGenerationTaskDTO projectGenerationTask = taskService.getTask(taskId);
    File outputFile = File.createTempFile(taskId, ".zip");
    outputFile.deleteOnExit();
    projectGenerationTask.setStorageLocation(outputFile.getAbsolutePath());
    projectGenerationTaskRepository.save(taskMapper.map(projectGenerationTask));
    try (InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(outputFile)) {
      // Java core has already a way to read all bytes from an input stream and write them into a
      // given output stream.
      is.transferTo(os);
    }
  }
}
