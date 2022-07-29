package com.celonis.challenge.dto;

public class TaskResultDTO<T> {

  private final T result;

  public TaskResultDTO(T result) {
    this.result = result;
  }

  public T getResult() {
    return result;
  }
}
