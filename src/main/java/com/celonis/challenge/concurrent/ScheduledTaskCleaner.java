package com.celonis.challenge.concurrent;

import com.celonis.challenge.services.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledTaskCleaner {

  private final TaskService taskService;

  // every week '@weekly'
  // every 5 minutes '0 */5 * * * *'
  @Scheduled(cron = "0 */1 * * * *")
  public void clean() {
    log.info("Started scheduled task cleaner");
    taskService.deleteNonExecutedTasks();
    log.info("Completed scheduled task cleaner");
  }
}
