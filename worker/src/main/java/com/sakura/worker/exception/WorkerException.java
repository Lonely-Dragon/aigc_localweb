package com.sakura.worker.exception;

import com.sakura.worker.common.BusinessException;
import com.sakura.worker.common.ResultCode;

/**
 * Worker模块自定义业务异常
 */
public class WorkerException extends BusinessException {

    public WorkerException(String message) {
        super(message);
    }

    public WorkerException(int code, String message) {
        super(code, message);
    }

    public WorkerException(ResultCode resultCode) {
        super(resultCode);
    }

    public WorkerException(ResultCode resultCode, String message) {
        super(resultCode, message);
    }

    public WorkerException(String message, Throwable cause) {
        super(message, cause);
    }
}
