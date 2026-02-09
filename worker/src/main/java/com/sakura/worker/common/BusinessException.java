package com.sakura.worker.common;

/**
 * 业务异常基类
 * 所有业务异常应继承此类，支持自定义错误码
 */
public class BusinessException extends RuntimeException {

    /** 错误码 */
    private final int code;

    /**
     * 使用默认业务错误码
     */
    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.BUSINESS_ERROR.getCode();
    }

    /**
     * 使用自定义错误码
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 使用 ResultCode 枚举
     */
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    /**
     * 使用 ResultCode 枚举 + 自定义消息
     */
    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
    }

    /**
     * 使用默认业务错误码 + 原始异常
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = ResultCode.BUSINESS_ERROR.getCode();
    }

    /**
     * 使用自定义错误码 + 原始异常
     */
    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
