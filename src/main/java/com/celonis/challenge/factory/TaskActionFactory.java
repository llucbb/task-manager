package com.celonis.challenge.factory;

import com.celonis.challenge.services.TaskActionService;

public interface TaskActionFactory {

  TaskActionService get(String taskActionServiceName);
}
