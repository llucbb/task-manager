package com.celonis.challenge.services.impl;

import com.celonis.challenge.exceptions.NotFoundException;
import com.celonis.challenge.factory.TaskExecutorServiceFactory;
import com.celonis.challenge.mapper.TaskMapper;
import com.celonis.challenge.model.Task;
import com.celonis.challenge.model.dto.TaskDTO;
import com.celonis.challenge.model.dto.TaskResultDTO;
import com.celonis.challenge.model.dto.TaskStatusDTO;
import com.celonis.challenge.repositories.TaskRepository;
import com.celonis.challenge.services.TaskExecutorService;
import com.celonis.challenge.services.TaskService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

  private static final ZoneId UTC = ZoneId.of("UTC");
  // One week
  private static final int DAYS_TO_KEEP_NON_EXECUTED_TASKS = 1;

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
  public void executeTask(String taskId) {
    TaskDTO task = getTask(taskId);
    taskExecutorServiceFactory.get(task.getType()).executeTask(task);
  }

  @Override
  public TaskStatusDTO getTaskStatus(String taskId) {
    TaskDTO task = getTask(taskId);
    return taskExecutorServiceFactory.get(task.getType()).getTaskStatus(task);
  }

  @Override
  public TaskResultDTO<?> getTaskResult(String taskId) {
    TaskDTO task = getTask(taskId);
    return taskExecutorServiceFactory.get(task.getType()).getTaskResult(task);
  }

  @Override
  public void cancel(String taskId) {
    TaskDTO task = getTask(taskId);
    taskExecutorServiceFactory.get(task.getType()).cancelTask(task);
  }

  @Override
  @Transactional
  public void deleteNonExecutedTasks() {
    LocalDateTime referenceDate =
        LocalDateTime.now(UTC).minusMinutes(DAYS_TO_KEEP_NON_EXECUTED_TASKS);
    // Get tasks created before the reference date
    List<Task> createdBeforeReferenceDateTasks =
        taskRepository.findByCreationDateLessThan(Date.from(referenceDate.atZone(UTC).toInstant()));

    Set<String> tasksIdsToDelete =
        createdBeforeReferenceDateTasks.stream()
            .filter(this::isNotExecutedOrCompleted)
            .map(Task::getId)
            .collect(Collectors.toSet());
    if (!CollectionUtils.isEmpty(tasksIdsToDelete)) {
      // Delete all tasks by ids in batch
      taskRepository.deleteAllByIdInBatch(tasksIdsToDelete);
      log.info(
          "Task clean service has deleted the following non executed tasks: " + tasksIdsToDelete);
    }
  }

  private boolean isNotExecutedOrCompleted(Task task) {
    TaskExecutorService taskExecutorService = taskExecutorServiceFactory.get(task.getType());
    TaskStatusDTO taskStatus = taskExecutorService.getTaskStatus(taskMapper.map(task));
    return !taskStatus.getCompleted() && !taskStatus.getExecuted();
  }
}
