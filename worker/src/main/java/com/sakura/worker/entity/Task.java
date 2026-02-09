package com.sakura.worker.entity;

import com.alibaba.fastjson2.JSONObject;
import com.sakura.worker.enums.TaskType;

/**
 * 任务实体类
 */
public class Task {
    
    /**
     * 任务类型
     */
    private String taskType;
    
    /**
     * 任务参数
     */
    private JSONObject params;

    public Task() {
    }

    public Task(String taskType, JSONObject params) {
        this.taskType = taskType;
        this.params = params;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject params) {
        this.params = params;
    }

    /**
     * 获取任务类型枚举
     * @return 任务类型枚举
     */
    public TaskType getTaskTypeEnum() {
        return TaskType.fromCode(taskType);
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskType='" + taskType + '\'' +
                ", params=" + params +
                '}';
    }
}
