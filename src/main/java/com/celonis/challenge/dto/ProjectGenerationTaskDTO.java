package com.celonis.challenge.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectGenerationTaskDTO extends TaskDTO {

  @JsonIgnore private String storageLocation;

  public boolean done() {
    return StringUtils.hasText(storageLocation);
  }
}
