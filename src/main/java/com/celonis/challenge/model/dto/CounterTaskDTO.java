package com.celonis.challenge.model.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
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
  @PositiveOrZero(message = "x can't be negative")
  private Integer x;

  // As the task will take y - x seconds to complete we need to limit its maximum value to avoid
  // memory
  // problems
  @NotNull(message = "y is mandatory")
  // @Max(value = 120, message = "y can't be greater than 60")
  private Integer y;
}
