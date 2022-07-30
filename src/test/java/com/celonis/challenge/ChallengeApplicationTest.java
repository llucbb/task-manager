package com.celonis.challenge;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.celonis.challenge.controllers.TaskController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChallengeApplicationTest {

  @Autowired private TaskController taskController;

  @Test
  public void contextLoads() {
    assertNotNull(taskController);
  }
}
