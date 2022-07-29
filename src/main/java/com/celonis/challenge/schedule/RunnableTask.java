package com.celonis.challenge.schedule;

import com.celonis.challenge.dto.CounterTaskDTO;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RunnableTask implements Runnable {

  private final CounterTaskDTO counterTask;
  private final AtomicBoolean running = new AtomicBoolean(false);

  public RunnableTask(CounterTaskDTO counterTask) {
    this.counterTask = counterTask;
  }

  public void stop() {
    running.set(false);
  }

  @Override
  public void run() {
    log.info("Started task {}", counterTask.getName());
    running.set(true);
    while (running.get()) {
      counterTask.incrementX();
      if (counterTask.getX() >= counterTask.getY()) {
        stop();
        log.info("Finish task {}", counterTask.getName());
      }
    }
  }
}
