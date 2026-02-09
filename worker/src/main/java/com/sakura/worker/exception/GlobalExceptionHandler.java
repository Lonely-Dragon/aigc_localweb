package com.sakura.worker.exception;

import com.sakura.worker.common.BusinessException;
import com.sakura.worker.common.Result;
import com.sakura.worker.common.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

/**
 * Worker模块 - 全局异常处理器
 * 统一拦截并处理所有异常，返回标准的 Result 响应格式
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ======================== 业务异常 ========================

    /**
     * 处理 Worker 模块自定义业务异常
     */
    @ExceptionHandler(WorkerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleWorkerException(WorkerException e, HttpServletRequest request) {
        log.warn("Worker业务异常 | URI: {} | 错误码: {} | 消息: {}", request.getRequestURI(), e.getCode(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理通用业务异常（BusinessException 基类）
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常 | URI: {} | 错误码: {} | 消息: {}", request.getRequestURI(), e.getCode(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    // ======================== 参数校验异常 ========================

    /**
     * 处理 @Valid 注解校验失败异常（RequestBody）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("参数校验失败 | URI: {} | 消息: {}", request.getRequestURI(), errorMsg);
        return Result.error(ResultCode.VALIDATION_ERROR, errorMsg);
    }

    /**
     * 处理 @Valid 注解校验失败异常（表单绑定）
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleBindException(BindException e, HttpServletRequest request) {
        String errorMsg = e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.warn("参数绑定失败 | URI: {} | 消息: {}", request.getRequestURI(), errorMsg);
        return Result.error(ResultCode.VALIDATION_ERROR, errorMsg);
    }

    /**
     * 处理缺少请求参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        String errorMsg = String.format("缺少必要参数: %s", e.getParameterName());
        log.warn("缺少请求参数 | URI: {} | 消息: {}", request.getRequestURI(), errorMsg);
        return Result.error(ResultCode.BAD_REQUEST, errorMsg);
    }

    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String errorMsg = String.format("参数 '%s' 类型不匹配，期望类型: %s",
                e.getName(), e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "未知");
        log.warn("参数类型不匹配 | URI: {} | 消息: {}", request.getRequestURI(), errorMsg);
        return Result.error(ResultCode.BAD_REQUEST, errorMsg);
    }

    /**
     * 处理请求体不可读异常（如 JSON 格式错误）
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.warn("请求体解析失败 | URI: {} | 消息: {}", request.getRequestURI(), e.getMessage());
        return Result.error(ResultCode.BAD_REQUEST, "请求体格式错误，请检查JSON格式");
    }

    // ======================== HTTP 请求异常 ========================

    /**
     * 处理请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String errorMsg = String.format("不支持 %s 请求方法", e.getMethod());
        log.warn("请求方法不支持 | URI: {} | 消息: {}", request.getRequestURI(), errorMsg);
        return Result.error(ResultCode.METHOD_NOT_ALLOWED, errorMsg);
    }

    /**
     * 处理资源不存在异常
     */
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<?> handleNoResourceFoundException(NoResourceFoundException e, HttpServletRequest request) {
        log.warn("资源不存在 | URI: {}", request.getRequestURI());
        return Result.error(ResultCode.NOT_FOUND, "请求的资源不存在");
    }

    // ======================== 兜底异常 ========================

    /**
     * 处理所有未捕获的异常（兜底）
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常 | URI: {} | 异常类型: {} | 消息: {}", request.getRequestURI(), e.getClass().getSimpleName(), e.getMessage(), e);
        return Result.error(ResultCode.INTERNAL_ERROR, "服务器内部错误，请稍后重试");
    }
}
