package com.celonis.challenge.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TaskResultDTO<T> {

  private final T result;

  public T getResult() {
    return result;
  }
}
