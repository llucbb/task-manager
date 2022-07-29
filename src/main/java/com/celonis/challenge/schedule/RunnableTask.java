package com.celonis.challenge.schedule;

import com.celonis.challenge.dto.CounterTaskDTO;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunnableTask implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(RunnableTask.class);

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
    LOG.info("Started task {}", counterTask.getName());
    running.set(true);
    while (running.get()) {
      counterTask.incrementX();
      if (counterTask.getX() >= counterTask.getY()) {
        stop();
        LOG.info("Finish task {}", counterTask.getName());
      }
    }
  }
}
