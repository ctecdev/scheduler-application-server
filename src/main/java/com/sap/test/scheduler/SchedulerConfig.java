package com.sap.test.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

//    final Logger LOGGER = LogManager.getLogger(SchedulerConfig.class);

    @Value("${thread.pool.task.scheduler.size}")
    private int poolSize;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
//        LOGGER.debug("Creating Async Task Scheduler");
        scheduledTaskRegistrar.setTaskScheduler(taskScheduler());
    }

    // This is mandatory otherwise it will to be able to find bean of
    // taskScheduler. Without this it was giving runtime error says, can not find
    // taskScheduler bean.
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(poolSize);
        scheduler.setThreadNamePrefix("ThreadPoolTaskScheduler-");
        scheduler.initialize();
        return scheduler;
    }
}