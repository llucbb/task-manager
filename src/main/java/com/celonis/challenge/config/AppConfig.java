package com.celonis.challenge.config;

import com.celonis.challenge.factory.TaskExecutorServiceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AppConfig {

  private static final int CORES = Runtime.getRuntime().availableProcessors();

  @Value("${application.counter-delay}")
  private int counterDelay;

  @Bean
  public ServiceLocatorFactoryBean taskExecutorServiceFactory() {
    ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
    factoryBean.setServiceLocatorInterface(TaskExecutorServiceFactory.class);
    return factoryBean;
  }

  @Bean
  public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    // The following values depend on many factors like the computer running the application, the number and rate of tasks executed, the delay applied to the counter
    // tasks, etc.
    // I've decided to use the number of cores of the computer and a pool size big enough to be able
    // to queue some tasks
    executor.setCorePoolSize(CORES);
    executor.setThreadNamePrefix(AppConstants.COUNTER_TASK);
    executor.initialize();
    return executor;
  }
}
