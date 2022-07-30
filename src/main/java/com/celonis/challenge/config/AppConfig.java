package com.celonis.challenge.config;

import com.celonis.challenge.factory.TaskExecutorServiceFactory;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AppConfig {

  private static final int CORES = Runtime.getRuntime().availableProcessors();

  @Bean
  public ServiceLocatorFactoryBean taskExecutorServiceFactory() {
    ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
    factoryBean.setServiceLocatorInterface(TaskExecutorServiceFactory.class);
    return factoryBean;
  }

  @Bean
  public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(CORES);
    executor.setMaxPoolSize(20);
    executor.setThreadNamePrefix(AppConstants.COUNTER_TASK.toLowerCase());
    executor.initialize();
    return executor;
  }
}
