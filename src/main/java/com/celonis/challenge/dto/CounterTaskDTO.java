package com.celonis.challenge.dto;

import com.celonis.challenge.enums.TaskType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class CounterTaskDTO extends TaskDTO {

  @Getter @Setter @NotNull private Integer x;

  @Getter @Setter @NotNull private Integer y;

  public void incrementX() {
    x++;
  }

  public TaskType getTaskType() {
    return TaskType.COUNTER;
  }
}
