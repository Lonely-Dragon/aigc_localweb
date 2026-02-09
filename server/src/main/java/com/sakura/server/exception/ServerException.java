package com.sakura.server.exception;

import com.sakura.worker.common.BusinessException;
import com.sakura.worker.common.ResultCode;

/**
 * Server模块自定义业务异常
 */
public class ServerException extends BusinessException {

    public ServerException(String message) {
        super(message);
    }

    public ServerException(int code, String message) {
        super(code, message);
    }

    public ServerException(ResultCode resultCode) {
        super(resultCode);
    }

    public ServerException(ResultCode resultCode, String message) {
        super(resultCode, message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
