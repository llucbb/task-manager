package com.celonis.challenge.services;

import com.celonis.challenge.dto.TaskDTO;

public interface TaskCreationService {

  TaskDTO createTask(TaskDTO task);
}
