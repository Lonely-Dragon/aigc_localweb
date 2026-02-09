package com.sakura.worker.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.sakura.worker.enums.TaskType;
import java.util.Map;

/**
 * 任务实体类
 * 对应Redis中存储的任务JSON结构
 */
public class Task {
    
    /**
     * 任务类型
     */
    @JSONField(name = "taskType")
    private String taskType;
    
    /**
     * 任务参数
     */
    @JSONField(name = "params")
    private Map<String, Object> params;

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    /**
     * 获取任务类型枚举
     * @return 任务类型枚举，如果不存在返回null
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
