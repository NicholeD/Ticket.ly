package com.kenzie.unit.four.ticketsystem.config;

import com.kenzie.unit.four.ticketsystem.service.model.ReservedTicket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ConcurrentLinkedQueue;

@Configuration
public class ExecutorServiceConfig {

    @Bean
    public ConcurrentLinkedQueue<ReservedTicket> reservedTicketsQueue() {
        return new ConcurrentLinkedQueue<>();
    }

    @Bean
    public TaskExecutor executorService() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setThreadNamePrefix("default_task_executor_thread");
        executor.initialize();
        return executor;
    }
}
