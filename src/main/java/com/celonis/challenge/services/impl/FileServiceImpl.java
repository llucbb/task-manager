package com.celonis.challenge.services.impl;

import com.celonis.challenge.exceptions.InternalException;
import com.celonis.challenge.exceptions.NotFoundException;
import com.celonis.challenge.model.ProjectGenerationTask;
import com.celonis.challenge.model.dto.ProjectGenerationTaskDTO;
import com.celonis.challenge.model.dto.TaskDTO;
import com.celonis.challenge.repositories.ProjectGenerationTaskRepository;
import com.celonis.challenge.services.FileService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

  private final ProjectGenerationTaskRepository projectGenerationTaskRepository;

  @Override
  @Transactional
  public void storeResult(TaskDTO task, URL url) throws IOException {
    ProjectGenerationTask projectGenerationTask =
        projectGenerationTaskRepository.findById(task.getId()).orElseThrow(NotFoundException::new);
    File outputFile = File.createTempFile(task.getId(), ".zip");
    outputFile.deleteOnExit();
    try (InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(outputFile)) {
      is.transferTo(os);
    }
    projectGenerationTask.setStorageLocation(outputFile.getAbsolutePath());
    projectGenerationTaskRepository.save(projectGenerationTask);
  }

  @Override
  public FileSystemResource getTaskResult(ProjectGenerationTaskDTO projectGenerationTask) {
    File inputFile = new File(projectGenerationTask.getStorageLocation());
    if (!inputFile.exists()) {
      throw new InternalException("File not generated yet");
    }

    return new FileSystemResource(inputFile);
  }
}
