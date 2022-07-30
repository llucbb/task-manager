package com.celonis.challenge.repositories;

import com.celonis.challenge.model.Task;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

  List<Task> findByCreationDateLessThan(Date referenceDate);
}
