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

  private static final String EVERY_SUNDAY_AT_3 = "0 0 3 * * 7";
  // private static final String EVERY_5_MINUTES= "0 */5 * * * *";
  private final TaskService taskService;

  @Scheduled(cron = EVERY_SUNDAY_AT_3)
  //@Scheduled(fixedRate = 60000)
  public void clean() {
    log.info("Started scheduled task cleaner");
    taskService.deleteNonExecutedTasks();
    log.info("Completed scheduled task cleaner");
  }
}
