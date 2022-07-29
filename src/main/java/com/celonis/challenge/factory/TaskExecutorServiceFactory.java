package com.celonis.challenge.factory;

import com.celonis.challenge.services.TaskExecutorService;

public interface TaskExecutorServiceFactory {

  TaskExecutorService get(String serviceName);
}
