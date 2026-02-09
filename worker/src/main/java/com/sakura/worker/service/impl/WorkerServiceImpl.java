package com.sakura.worker.service.impl;

import com.sakura.worker.service.WorkerService;
import org.springframework.stereotype.Service;

/**
 * Worker服务实现类
 */
@Service
public class WorkerServiceImpl implements WorkerService {
    
    /**
     * 执行工作任务
     * @param task 任务内容
     * @return 处理结果
     */
    @Override
    public String processTask(String task) {
        return "Worker处理任务: " + task;
    }

    /**
     * 启动Worker
     */
    @Override
    public void start() {
        System.out.println("Worker已启动");
    }

    /**
     * 停止Worker
     */
    @Override
    public void stop() {
        System.out.println("Worker已停止");
    }
}
