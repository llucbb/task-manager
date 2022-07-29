package com.celonis.challenge.services.impl;

import com.celonis.challenge.dto.TaskDTO;
import com.celonis.challenge.dto.TaskResultDTO;
import com.celonis.challenge.exceptions.NotFoundException;
import com.celonis.challenge.mapper.TaskMapper;
import com.celonis.challenge.model.Task;
import com.celonis.challenge.repositories.TaskRepository;
import com.celonis.challenge.services.TaskCreationFactory;
import com.celonis.challenge.services.TaskCreationService;
import com.celonis.challenge.services.TaskExecutorFactory;
import com.celonis.challenge.services.TaskExecutorService;
import com.celonis.challenge.services.TaskService;
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
  private final TaskCreationFactory taskCreationFactory;
  private final TaskExecutorFactory taskExecutorFactory;
  private final TaskRepository taskRepository;

  @Override
  @Transactional
  public TaskDTO createTask(TaskDTO task) {
    TaskCreationService taskCreationService =
        taskCreationFactory.get(task.getTaskType().name() + "_CREATE");
    return taskCreationService.createTask(task);
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

    return task.map(taskMapper::map).orElseThrow(NotFoundException::new);
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
    TaskExecutorService taskExecutorService =
        taskExecutorFactory.get(task.getTaskType() + "_EXECUTE");
    taskExecutorService.executeTask(task);
  }

  @Override
  @Transactional
  public TaskResultDTO<?> getTaskResult(String taskId) {
    TaskDTO task = getTask(taskId);
    TaskExecutorService taskExecutorService =
        taskExecutorFactory.get(task.getTaskType() + "_EXECUTE");
    return taskExecutorService.getTaskResult(task);
  }
}
