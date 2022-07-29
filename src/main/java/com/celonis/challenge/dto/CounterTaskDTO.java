package com.celonis.challenge.dto;

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

  @NotNull private Integer x;

  @NotNull private Integer y;

  public boolean done() {
    return x >= y;
  }
}
