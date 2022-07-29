package com.celonis.challenge.exceptions;

public class TaskFailureException extends RuntimeException {

  public TaskFailureException(String message, Throwable cause) {
    super(message, cause);
  }

  public TaskFailureException(String message) {
    super(message);
  }
}
