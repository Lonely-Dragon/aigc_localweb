package com.sakura.server.exception;

/**
 * Server自定义异常
 */
public class ServerException extends RuntimeException {
    
    public ServerException(String message) {
        super(message);
    }
    
    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
