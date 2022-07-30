package com.celonis.challenge.dto;

import javax.validation.constraints.Max;
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

  // We want to avoid negative number for simplicity
  @NotNull(message = "x is mandatory")
  @Min(value = 0, message = "x can't be negative")
  private Integer x;

  // 60 is maximum default keepAliveSeconds of ThreadPoolTaskExecutor. We also need to have a limit
  // to not have memory problems
  @NotNull(message = "y is mandatory")
  @Max(value = 60, message = "y can't be greater than 60")
  private Integer y;

  public boolean done() {
    return x >= y;
  }
}
