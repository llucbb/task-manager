package com.celonis.challenge.config;

import com.celonis.challenge.exceptions.NotAuthorizedException;
import com.celonis.challenge.exceptions.NotFoundException;
import com.celonis.challenge.exceptions.TaskException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  public String handleNotFound(NotFoundException e) {
    log.warn("Entity not found", e);
    return "Not found";
  }

  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(NotAuthorizedException.class)
  public String handleNotAuthorized() {
    log.warn("Not authorized");
    return "Not authorized";
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(TaskException.class)
  public String handleTaskException(TaskException e) {
    log.error("Task error", e);
    return e.getMessage();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public String handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
    log.error("Method argument not valid", e);
    FieldError fieldError = e.getFieldError();
    return fieldError != null ? fieldError.getDefaultMessage() : "Method argument not valid";
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public String handleInternalError(Exception e) {
    log.error("Unhandled Exception in Controller", e);
    return "Internal error";
  }
}
