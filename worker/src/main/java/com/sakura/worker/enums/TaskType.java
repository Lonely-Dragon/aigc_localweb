package com.sakura.worker.enums;

/**
 * 任务类型枚举
 */
public enum TaskType {
    
    /**
     * 图片类型任务
     */
    PIC_TYPE("PIC_TYPE", "图片类型任务");

    private final String code;
    private final String description;

    TaskType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据code获取任务类型
     * @param code 任务类型代码
     * @return 任务类型枚举，如果不存在返回null
     */
    public static TaskType fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (TaskType type : TaskType.values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 判断code是否存在
     * @param code 任务类型代码
     * @return 是否存在
     */
    public static boolean isValid(String code) {
        return fromCode(code) != null;
    }
}
