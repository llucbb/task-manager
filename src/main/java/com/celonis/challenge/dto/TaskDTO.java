package com.celonis.challenge.dto;

import com.celonis.challenge.enums.TaskType;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO implements TaskTypified {

  private String id;

  @NotBlank private String name;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss z")
  private Date creationDate;

  @Override
  public TaskType getTaskType() {
    throw null;
  }
}
