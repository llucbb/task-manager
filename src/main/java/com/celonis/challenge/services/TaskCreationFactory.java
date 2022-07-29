package com.celonis.challenge.services;

public interface TaskCreationFactory {

  TaskCreationService get(String taskType);
}
