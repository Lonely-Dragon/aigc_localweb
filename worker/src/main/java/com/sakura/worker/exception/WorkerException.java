package com.sakura.worker.exception;

/**
 * Worker自定义异常
 */
public class WorkerException extends RuntimeException {
    
    public WorkerException(String message) {
        super(message);
    }
    
    public WorkerException(String message, Throwable cause) {
        super(message, cause);
    }
}
