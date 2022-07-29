package com.celonis.challenge.dto;

import com.celonis.challenge.enums.TaskType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class ProjectGenerationTaskDTO extends TaskDTO {

  @Getter @Setter @JsonIgnore private String storageLocation;

  public TaskType getTaskType() {
    return TaskType.PROJECT_GENERATION;
  }
}