package com.sakura.worker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;

/**
 * 任务轮询线程池配置
 */
@Configuration
public class TaskPollingConfig {

    /**
     * 创建一个小型定时任务线程池用于任务轮询
     * 线程池大小：3个线程
     */
    @Bean(name = "taskPollingScheduler")
    public ScheduledExecutorService taskPollingScheduler() {
        return Executors.newScheduledThreadPool(3, r -> {
            Thread thread = new Thread(r, "task-polling-scheduler");
            thread.setDaemon(true);
            return thread;
        });
    }
}
