package com.celonis.challenge.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotFoundException extends RuntimeException {

  public NotFoundException(Throwable cause) {
    super(cause);
  }
}
