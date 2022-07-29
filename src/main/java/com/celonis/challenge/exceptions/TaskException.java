package com.celonis.challenge.exceptions;

public class TaskException extends RuntimeException {

  public TaskException(String message) {
    super(message);
  }

  public TaskException(Throwable cause) {
    super(cause);
  }
}
