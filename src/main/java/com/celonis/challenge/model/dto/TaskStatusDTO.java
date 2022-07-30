package com.celonis.challenge.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusDTO {

  private TaskDTO task;
  private Boolean executed;
  private Boolean completed;
}
