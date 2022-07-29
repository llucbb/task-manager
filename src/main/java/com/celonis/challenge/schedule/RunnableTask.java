package com.celonis.challenge.schedule;

import com.celonis.challenge.dto.CounterTaskDTO;
import com.celonis.challenge.exceptions.InternalException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RunnableTask implements Runnable {

  private final CounterTaskDTO counterTask;
  private final AtomicInteger x;
  private final AtomicBoolean running = new AtomicBoolean(false);
  private final int y;

  public RunnableTask(CounterTaskDTO counterTask) {
    x = new AtomicInteger(counterTask.getX());
    y = counterTask.getY();
    this.counterTask = counterTask;
  }

  public void stop() {
    running.set(false);
    Thread.currentThread().interrupt();
    log.info("Stopped task {}", counterTask.getName());
  }

  @Override
  public void run() {
    log.info("Started task {}", counterTask.getName());
    running.set(true);
    while (running.get()) {
      log.info("x: {} at task {}", x.get(), counterTask.getName());
      if (x.get() < y) {
        x.getAndIncrement();
        try {
          Thread.sleep(5000);
        } catch (InterruptedException e) {
          throw new InternalException(e);
        }
      } else {
        stop();
      }
    }
  }
}
