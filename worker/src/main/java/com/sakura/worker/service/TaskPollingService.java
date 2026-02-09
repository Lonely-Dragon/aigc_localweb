package com.sakura.worker.service;

import com.alibaba.fastjson2.JSON;
import com.sakura.worker.dto.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 任务轮询服务
 * 使用ScheduledExecutorService定期轮询server端的任务列表
 */
@Service
public class TaskPollingService implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(TaskPollingService.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskHandlerService taskHandlerService;

    @Autowired
    @Qualifier("taskPollingScheduler")
    private ScheduledExecutorService taskPollingScheduler;

    @Value("${task.polling.interval:5000}")
    private long pollingInterval; // 轮询间隔，默认5秒

    @Value("${task.polling.enabled:true}")
    private boolean pollingEnabled; // 是否启用轮询，默认启用

    // 保存定时任务的Future引用，用于取消任务
    private final AtomicReference<ScheduledFuture<?>> scheduledTaskRef = new AtomicReference<>();

    /**
     * 执行一次轮询任务
     */
    private void pollTask() {
        try {
            // 轮询server端的任务列表
            Object taskObj = taskService.getFirstTask();
            
            if (taskObj != null && !"键不存在或数组为空".equals(taskObj.toString())) {
                logger.info("获取到任务: {}", taskObj);
                
                try {
                    // 解析JSON任务对象
                    Task task = parseTask(taskObj);
                    if (task == null) {
                        logger.error("任务解析失败，跳过此任务");
                        return;
                    }
                    
                    logger.info("解析任务成功，任务类型: {}, 参数: {}", task.getTaskType(), task.getParams());
                    
                    // 根据任务类型调用对应的处理器处理任务
                    String result = taskHandlerService.processTask(task);
                    logger.info("任务处理完成，结果: {}", result);
                    
                    // 删除已完成的任务
                    String removeResult = taskService.removeCompletedTask();
                    logger.info("已删除已完成的任务: {}", removeResult);
                } catch (Exception e) {
                    logger.error("处理任务时发生异常: {}", e.getMessage());
                    // 处理失败时不删除任务，保留在队列中以便重试
                }
            } else {
                logger.debug("当前没有待处理的任务");
            }
        } catch (Exception e) {
            logger.error("轮询任务时发生异常: {}", e.getMessage());
        }
    }

    /**
     * 解析任务对象
     * @param taskObj 从Redis获取的任务对象（可能是Map、JSON字符串等）
     * @return Task对象，解析失败返回null
     */
    private Task parseTask(Object taskObj) {
        try {
            // 如果已经是Task对象，直接返回
            if (taskObj instanceof Task) {
                return (Task) taskObj;
            }
            
            // 如果是字符串，尝试解析JSON
            if (taskObj instanceof String) {
                String jsonStr = (String) taskObj;
                return JSON.parseObject(jsonStr, Task.class);
            }
            
            // 如果是Map或其他类型，转换为JSON字符串再解析
            String jsonStr = JSON.toJSONString(taskObj);
            return JSON.parseObject(jsonStr, Task.class);
        } catch (Exception e) {
            logger.error("解析任务对象失败: {}, 错误信息: {}", taskObj, e.getMessage());
            return null;
        }
    }

    /**
     * 启动轮询任务
     */
    public void startPolling() {
        if (!pollingEnabled) {
            logger.info("任务轮询功能已禁用");
            return;
        }

        // 如果已经有任务在运行，先取消
        ScheduledFuture<?> existingTask = scheduledTaskRef.get();
        if (existingTask != null && !existingTask.isCancelled()) {
            logger.warn("任务轮询服务已经在运行中，先取消旧任务");
            existingTask.cancel(false);
        }

        logger.info("开始启动任务轮询服务，轮询间隔: {}ms", pollingInterval);
        
        // 使用scheduleAtFixedRate定期执行轮询任务
        // 初始延迟0秒，之后每隔pollingInterval毫秒执行一次
        ScheduledFuture<?> scheduledTask = taskPollingScheduler.scheduleAtFixedRate(
            this::pollTask,
            0,
            pollingInterval,
            TimeUnit.MILLISECONDS
        );
        
        scheduledTaskRef.set(scheduledTask);
        logger.info("任务轮询服务已启动");
    }

    /**
     * 停止轮询任务
     */
    public void stopPolling() {
        ScheduledFuture<?> scheduledTask = scheduledTaskRef.get();
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            logger.info("正在停止任务轮询服务...");
            boolean cancelled = scheduledTask.cancel(false);
            if (cancelled) {
                logger.info("任务轮询服务已停止");
            } else {
                logger.warn("取消任务轮询失败，任务可能已完成");
            }
            scheduledTaskRef.set(null);
        }
    }

    /**
     * 应用启动时自动启动轮询
     */
    @Override
    public void run(String... args) {
        startPolling();
    }

    /**
     * 应用关闭时优雅地停止轮询
     */
    @PreDestroy
    public void destroy() {
        stopPolling();
        shutdownScheduler();
    }

    /**
     * 关闭定时任务线程池
     */
    private void shutdownScheduler() {
        if (taskPollingScheduler != null && !taskPollingScheduler.isShutdown()) {
            logger.info("正在关闭任务轮询线程池...");
            taskPollingScheduler.shutdown();
            try {
                // 等待5秒让正在执行的任务完成
                if (!taskPollingScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    logger.warn("线程池在5秒内未能正常关闭，强制关闭");
                    taskPollingScheduler.shutdownNow();
                    // 再等待5秒
                    if (!taskPollingScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                        logger.error("线程池未能正常关闭");
                    }
                }
            } catch (InterruptedException e) {
                logger.error("等待线程池关闭时被中断", e);
                taskPollingScheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
            logger.info("任务轮询线程池已关闭");
        }
    }
}
