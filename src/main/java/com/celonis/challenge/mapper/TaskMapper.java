package com.celonis.challenge.mapper;

import com.celonis.challenge.dto.ProjectGenerationTaskDTO;
import com.celonis.challenge.model.ProjectGenerationTask;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

  ProjectGenerationTask map(ProjectGenerationTaskDTO projectGenerationTask);

  ProjectGenerationTaskDTO map(ProjectGenerationTask projectGenerationTask);

  List<ProjectGenerationTaskDTO> map(List<ProjectGenerationTask> projectGenerationTasks);
}
