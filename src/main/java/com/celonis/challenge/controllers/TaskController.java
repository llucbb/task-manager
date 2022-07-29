package com.celonis.challenge.controllers;

import com.celonis.challenge.dto.CounterTaskDTO;
import com.celonis.challenge.dto.ProjectGenerationTaskDTO;
import com.celonis.challenge.dto.TaskDTO;
import com.celonis.challenge.dto.TaskStatusDTO;
import com.celonis.challenge.services.TaskService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
public class TaskController {

  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @PostMapping("/")
  public TaskDTO createTask(@RequestBody @Valid ProjectGenerationTaskDTO task) {
    return taskService.createTask(task);
  }

  @GetMapping("/")
  public List<TaskDTO> listTasks() {
    return taskService.listTasks();
  }

  @PostMapping("/counter")
  public TaskDTO createTask(@RequestBody @Valid CounterTaskDTO task) {
    return taskService.createTask(task);
  }

  @GetMapping("/{taskId}")
  public TaskDTO getTask(@PathVariable String taskId) {
    return taskService.getTask(taskId);
  }

  @PutMapping("/{taskId}")
  public TaskDTO updateTask(@PathVariable String taskId, @RequestBody @Valid TaskDTO task) {
    return taskService.update(taskId, task);
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

  @GetMapping("/{taskId}/status")
  public TaskStatusDTO getTaskStatus(@PathVariable String taskId) {
    return taskService.getTaskStatus(taskId);
  }

  @PutMapping("/{taskId}/cancel")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void cancelTask(@PathVariable String taskId) {
    taskService.cancel(taskId);
  }

  @GetMapping("/{taskId}/result")
  public ResponseEntity<FileSystemResource> getResult(@PathVariable String taskId) {
    FileSystemResource taskResult =
        ((FileSystemResource) taskService.getTaskResult(taskId).getResult());

    HttpHeaders respHeaders = new HttpHeaders();
    respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    respHeaders.setContentDispositionFormData("attachment", taskResult.getFilename());
    return new ResponseEntity<>(taskResult, respHeaders, HttpStatus.OK);
  }
}
