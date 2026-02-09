package com.sakura.worker.service;

/**
 * 任务服务接口，负责调用Server端的接口
 */
public interface TaskService {

    /**
     * 通过调用server端的接口获取PIC_TASK的JSON数组第一个元素
     * @return 第一个元素的内容
     */
    Object getFirstTask();

    /**
     * 调用server端接口，删除已完成的任务（删除JSON数组的第一个元素）
     * @return 删除结果
     */
    String removeCompletedTask();
}
