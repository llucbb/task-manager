package com.celonis.challenge.concurrent;

import com.celonis.challenge.dto.CounterTaskDTO;
import com.celonis.challenge.exceptions.InternalException;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CallableTask implements Callable<CounterTaskDTO> {

  @Getter private final AtomicInteger x;
  @Getter private final int y;
  @Getter private final CounterTaskDTO counterTask;
  private final int counterDelayMs;

  public CallableTask(CounterTaskDTO counterTask, int counterDelayMs) {
    x = new AtomicInteger(counterTask.getX());
    y = counterTask.getY();
    this.counterTask = counterTask;
    this.counterDelayMs = counterDelayMs;
  }

  public void complete() {
    log.info("Completed task {}", counterTask.getName());
    Thread.currentThread().interrupt();
  }

  @Override
  public CounterTaskDTO call() {
    log.info("Started task {}", counterTask.getName());
    while (x.get() <= y) {
      log.info("x: {} at task {}", x.getAndIncrement(), counterTask.getName());
      try {
        Thread.sleep(counterDelayMs);
      } catch (InterruptedException e) {
        throw new InternalException(e);
      }
    }
    complete();
    counterTask.setX(x.get());
    return counterTask;
  }
}
