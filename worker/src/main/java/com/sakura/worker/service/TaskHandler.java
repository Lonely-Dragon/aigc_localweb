package com.sakura.worker.service;

import com.sakura.worker.dto.Task;

/**
 * 任务处理器接口
 * 不同任务类型需要实现此接口来处理具体任务
 */
public interface TaskHandler {
    
    /**
     * 处理任务
     * @param task 任务对象
     * @return 处理结果
     */
    String handle(Task task);
    
    /**
     * 获取支持的任务类型
     * @return 任务类型代码
     */
    String getSupportedTaskType();
}
