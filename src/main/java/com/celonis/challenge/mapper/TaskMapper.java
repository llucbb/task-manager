package com.celonis.challenge.mapper;

import static com.celonis.challenge.config.AppConstants.UNEXPECTED_TASK_TYPE;

import com.celonis.challenge.exceptions.InternalException;
import com.celonis.challenge.model.CounterTask;
import com.celonis.challenge.model.ProjectGenerationTask;
import com.celonis.challenge.model.Task;
import com.celonis.challenge.model.dto.CounterTaskDTO;
import com.celonis.challenge.model.dto.ProjectGenerationTaskDTO;
import com.celonis.challenge.model.dto.TaskDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.springframework.util.CollectionUtils;

@Mapper(componentModel = "spring")
public interface TaskMapper {

  default TaskDTO map(Task task) {
    if (task instanceof ProjectGenerationTask) {
      return map((ProjectGenerationTask) task);
    } else if (task instanceof CounterTask) {
      return map((CounterTask) task);
    } else {
      throw new InternalException(UNEXPECTED_TASK_TYPE);
    }
  }

  default Task map(TaskDTO task) {
    if (task instanceof ProjectGenerationTaskDTO) {
      return map((ProjectGenerationTaskDTO) task);
    } else if (task instanceof CounterTaskDTO) {
      return map((CounterTaskDTO) task);
    } else {
      throw new InternalException(UNEXPECTED_TASK_TYPE);
    }
  }

  default List<TaskDTO> map(List<Task> tasks) {
    if (!CollectionUtils.isEmpty(tasks)) {
      List<TaskDTO> result =
          tasks.stream()
              .filter(t -> t instanceof ProjectGenerationTask)
              .map(t -> map((ProjectGenerationTask) t))
              .collect(Collectors.toList());
      result.addAll(
          tasks.stream()
              .filter(t -> t instanceof CounterTask)
              .map(t -> map((CounterTask) t))
              .collect(Collectors.toList()));
      return result;
    } else {
      return List.of();
    }
  }

  ProjectGenerationTaskDTO map(ProjectGenerationTask task);

  ProjectGenerationTask map(ProjectGenerationTaskDTO task);

  CounterTaskDTO map(CounterTask task);

  CounterTask map(CounterTaskDTO task);
}
