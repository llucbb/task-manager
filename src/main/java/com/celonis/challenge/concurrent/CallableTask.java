package com.celonis.challenge.concurrent;

import com.celonis.challenge.dto.CounterTaskDTO;
import com.celonis.challenge.exceptions.InternalException;
import java.util.concurrent.Callable;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CallableTask implements Callable<CounterTaskDTO> {

  @Getter private final CounterTaskDTO counterTask;
  private final int delayMs;

  public CallableTask(CounterTaskDTO counterTask, int delayMs) {
    this.counterTask = counterTask;
    this.delayMs = delayMs;
  }

  @Override
  public CounterTaskDTO call() {
    String taskName = counterTask.getName();
    log.info("Started task {}", taskName);
    int x = counterTask.getX();
    final int y = counterTask.getY();
    while (x < y) {
      x++;
      log.info("x: {} at task {}", x, taskName);
      try {
        Thread.sleep(delayMs);
      } catch (InterruptedException e) {
        throw new InternalException(e);
      }
    }
    counterTask.setX(x);
    log.info("Completed task {}", counterTask.getName());
    return counterTask;
  }
}
