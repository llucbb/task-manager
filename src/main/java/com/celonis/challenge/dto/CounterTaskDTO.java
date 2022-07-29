package com.celonis.challenge.dto;

import com.celonis.challenge.enums.TaskType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CounterTaskDTO extends TaskDTO {

  @NotNull
  @Min(1)
  private Integer x;

  @NotNull
  @Min(1)
  private Integer y;

  public TaskType getTaskType() {
    return TaskType.COUNTER;
  }
}
