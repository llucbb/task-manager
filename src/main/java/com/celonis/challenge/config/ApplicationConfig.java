package com.celonis.challenge.config;

import com.celonis.challenge.factory.TaskActionFactory;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ApplicationConfig {

  @Bean("taskActionFactory")
  public ServiceLocatorFactoryBean taskActionFactoryBean() {
    ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
    factoryBean.setServiceLocatorInterface(TaskActionFactory.class);
    return factoryBean;
  }

  @Bean
  public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
    ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
    threadPoolTaskScheduler.setPoolSize(5);
    threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
    return threadPoolTaskScheduler;
  }
}
