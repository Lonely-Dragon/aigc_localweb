package com.sakura.worker.service;

/**
 * Worker服务接口
 */
public interface WorkerService {
    
    /**
     * 执行工作任务
     * @param task 任务内容
     * @return 处理结果
     */
    String processTask(String task);

    /**
     * 启动Worker
     */
    void start();

    /**
     * 停止Worker
     */
    void stop();
}
