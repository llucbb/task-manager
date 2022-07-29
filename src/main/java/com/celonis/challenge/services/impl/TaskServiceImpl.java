package com.celonis.challenge.services.impl;

import com.celonis.challenge.dto.TaskDTO;
import com.celonis.challenge.dto.TaskResultDTO;
import com.celonis.challenge.dto.TaskStatusDTO;
import com.celonis.challenge.exceptions.NotFoundException;
import com.celonis.challenge.factory.TaskExecutorServiceFactory;
import com.celonis.challenge.mapper.TaskMapper;
import com.celonis.challenge.model.Task;
import com.celonis.challenge.repositories.TaskRepository;
import com.celonis.challenge.services.TaskService;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

  private final TaskMapper taskMapper;
  private final TaskExecutorServiceFactory taskExecutorServiceFactory;
  private final TaskRepository taskRepository;

  @Override
  public TaskDTO createTask(TaskDTO task) {
    task.setId(null);
    task.setCreationDate(new Date());
    return taskMapper.map(taskRepository.save(taskMapper.map(task)));
  }

  @Override
  @Transactional(readOnly = true)
  public List<TaskDTO> listTasks() {
    return taskMapper.map(taskRepository.findAll());
  }

  @Override
  @Transactional(readOnly = true)
  public TaskDTO getTask(String taskId) {
    Optional<Task> task = taskRepository.findById(taskId);
    if (task.isEmpty()) {
      throw new NotFoundException("Task not found, id: " + taskId);
    } else {
      return taskMapper.map(task.get());
    }
  }

  @Override
  @Transactional
  public TaskDTO update(String taskId, TaskDTO task) {
    TaskDTO existing = getTask(taskId);
    existing.setCreationDate(task.getCreationDate());
    existing.setName(task.getName());
    return taskMapper.map(taskRepository.save(taskMapper.map(existing)));
  }

  @Override
  @Transactional
  public void delete(String taskId) {
    try {
      taskRepository.deleteById(taskId);
    } catch (EmptyResultDataAccessException e) {
      throw new NotFoundException(e);
    }
  }

  @Override
  @Transactional
  public void executeTask(String taskId) {
    TaskDTO task = getTask(taskId);
    taskExecutorServiceFactory.get(task.getType()).executeTask(task);
  }

  @Override
  @Transactional(readOnly = true)
  public TaskStatusDTO getTaskStatus(String taskId) {
    TaskDTO task = getTask(taskId);
    return taskExecutorServiceFactory.get(task.getType()).getTaskStatus(task);
  }

  @Override
  @Transactional
  public TaskResultDTO<?> getTaskResult(String taskId) {
    TaskDTO task = getTask(taskId);
    return taskExecutorServiceFactory.get(task.getType()).getTaskResult(task);
  }

  @Override
  public void cancel(String taskId) {
    TaskDTO task = getTask(taskId);
    taskExecutorServiceFactory.get(task.getType()).cancelTask(task);
  }
}
