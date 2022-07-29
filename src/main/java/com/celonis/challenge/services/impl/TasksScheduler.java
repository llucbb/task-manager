package com.celonis.challenge.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

// @Component
public class TasksScheduler {

  // @Scheduled(fixedRate = 1000)
  public void reportCurrentTime() {
    ZoneId zoneId = ZoneId.of("UTC");
    LocalDateTime now = LocalDateTime.now(zoneId);
    long epochSeconds = now.toEpochSecond(ZoneOffset.UTC);
    // LOG.info("The time is now {} and epoch is {} sec.",
    // now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), epochSeconds);
  }
}
