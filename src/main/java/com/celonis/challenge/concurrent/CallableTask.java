package com.celonis.challenge.concurrent;

import com.celonis.challenge.exceptions.InternalException;
import com.celonis.challenge.model.dto.CounterTaskDTO;
import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CallableTask implements Callable<CounterTaskDTO> {

  private final CounterTaskDTO counterTask;
  private final long delay;

  public CallableTask(CounterTaskDTO counterTask, long delay) {
    this.counterTask = counterTask;
    this.delay = delay;
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
        Thread.sleep(delay);
      } catch (InterruptedException e) {
        throw new InternalException(e);
      }
    }
    counterTask.setX(x);
    log.info("Completed task {}", counterTask.getName());
    return counterTask;
  }
}
