package com.celonis.challenge.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.celonis.challenge.factory.TaskExecutorServiceFactory;
import com.celonis.challenge.mapper.TaskMapper;
import com.celonis.challenge.model.Task;
import com.celonis.challenge.model.dto.TaskDTO;
import com.celonis.challenge.repositories.TaskRepository;
import com.celonis.challenge.services.impl.TaskServiceImpl;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

  private TaskService taskService;

  private @Spy TaskMapper taskMapper;
  private @Mock TaskExecutorServiceFactory taskExecutorServiceFactory;
  private @Mock TaskRepository taskRepository;

  @BeforeEach
  public void init() {
    taskService = new TaskServiceImpl(taskMapper, taskExecutorServiceFactory, taskRepository);
  }

  @Test
  public void testListTasks() {
    when(taskRepository.findAll()).thenReturn(List.of(new Task()));

    List<TaskDTO> result = taskService.listTasks();

    assertNotNull(result);
  }

  @Test
  public void testCreateTask() {
    TaskDTO task = new TaskDTO();

    //    TaskDTO result = taskService.createTask(task);

    //    assertNotNull(result);
  }

  @Test
  public void testGetTask() {
    String taskId = "";
    //    TaskDTO result = taskService.getTask(taskId);
  }

  @Test
  public void testUpdate() {
    String taskId = "";
    TaskDTO task = new TaskDTO();
    //    TaskDTO result = taskService.update(taskId, task);
  }

  @Test
  public void testDelete() {
    String taskId = "";
    //    taskService.delete(taskId);
  }

  @Test
  public void testExecuteTask() {
    String taskId = "";
    //    taskService.executeTask(taskId);
  }

  @Test
  public void getTaskStatus() {
    String taskId = "";
    //    TaskStatusDTO result = taskService.getTaskStatus(taskId);
  }

  @Test
  public void getTaskResult() {
    String taskId = "";
    //    TaskResultDTO<?> result = taskService.getTaskResult(taskId);
  }

  @Test
  public void testCancel() {
    String taskId = "";
    //    taskService.cancel(taskId);
  }

  @Test
  public void deleteNonExecutedTasks() {
    //    taskService.deleteNonExecutedTasks();
  }
}
