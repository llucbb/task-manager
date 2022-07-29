package com.celonis.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusDTO {

  private Boolean executed;
  private Boolean done;
  private Boolean cancelled;
}
