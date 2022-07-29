package com.celonis.challenge.repositories;

import com.celonis.challenge.model.CounterTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounterTaskRepository extends JpaRepository<CounterTask, String> {}
