package com.sakura.worker.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 任务请求DTO
 */
public class TaskRequest {
    
    @NotBlank(message = "任务名称不能为空")
    private String taskName;
    
    @NotBlank(message = "任务内容不能为空")
    private String taskContent;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }
}
