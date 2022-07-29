package com.celonis.challenge.exceptions;

public class TaskFailureException extends RuntimeException {

  public TaskFailureException(String message) {
    super(message);
  }

  public TaskFailureException(Throwable cause) {
    super(cause);
  }
}
