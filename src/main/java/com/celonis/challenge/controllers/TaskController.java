package com.celonis.challenge.controllers;

import com.celonis.challenge.dto.ProjectGenerationTaskDTO;
import com.celonis.challenge.services.TaskService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

  private final TaskService taskService;

  @GetMapping("/")
  public List<ProjectGenerationTaskDTO> listTasks() {
    return taskService.listTasks();
  }

  @PostMapping("/")
  public ProjectGenerationTaskDTO createTask(
      @RequestBody @Valid ProjectGenerationTaskDTO projectGenerationTask) {
    return taskService.createTask(projectGenerationTask);
  }

  @GetMapping("/{taskId}")
  public ProjectGenerationTaskDTO getTask(@PathVariable String taskId) {
    return taskService.getTask(taskId);
  }

  @PutMapping("/{taskId}")
  public ProjectGenerationTaskDTO updateTask(
      @PathVariable String taskId,
      @RequestBody @Valid ProjectGenerationTaskDTO projectGenerationTask) {
    return taskService.update(taskId, projectGenerationTask);
  }

  @DeleteMapping("/{taskId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTask(@PathVariable String taskId) {
    taskService.delete(taskId);
  }

  @PostMapping("/{taskId}/execute")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void executeTask(@PathVariable String taskId) {
    taskService.executeTask(taskId);
  }

  @GetMapping("/{taskId}/result")
  public ResponseEntity<FileSystemResource> getResult(@PathVariable String taskId) {
    return taskService.getTaskResult(taskId);
  }
}
