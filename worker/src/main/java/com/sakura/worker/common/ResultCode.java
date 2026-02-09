package com.sakura.worker.common;

/**
 * 统一响应状态码枚举
 */
public enum ResultCode {

    // ======================== 成功 ========================
    SUCCESS(200, "操作成功"),

    // ======================== 客户端错误 4xx ========================
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权，请先登录"),
    FORBIDDEN(403, "权限不足，拒绝访问"),
    NOT_FOUND(404, "请求资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    CONFLICT(409, "数据冲突"),
    VALIDATION_ERROR(422, "参数校验失败"),

    // ======================== 服务端错误 5xx ========================
    INTERNAL_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务暂时不可用"),

    // ======================== 业务错误 1xxx ========================
    BUSINESS_ERROR(1000, "业务处理失败"),
    TASK_NOT_FOUND(1001, "任务不存在"),
    TASK_PUSH_FAILED(1002, "任务推送失败"),
    TASK_PROCESS_FAILED(1003, "任务处理失败"),
    REDIS_OPERATION_FAILED(1004, "Redis操作失败");

    /** 状态码 */
    private final int code;

    /** 描述消息 */
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
