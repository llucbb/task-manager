package com.celonis.challenge.services;

public interface TaskExecutorFactory {

  TaskExecutorService get(String taskType);
}
