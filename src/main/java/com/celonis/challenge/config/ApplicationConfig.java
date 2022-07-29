package com.celonis.challenge.config;

import com.celonis.challenge.factory.TaskActionFactory;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ApplicationConfig {

  private static final int CORES = Runtime.getRuntime().availableProcessors();

  @Bean("taskActionFactory")
  public ServiceLocatorFactoryBean taskActionFactoryBean() {
    ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
    factoryBean.setServiceLocatorInterface(TaskActionFactory.class);
    return factoryBean;
  }

  @Bean
  public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(CORES);
    executor.setMaxPoolSize(20);
    executor.setThreadNamePrefix("task-executor");
    executor.initialize();
    return executor;
  }
}
