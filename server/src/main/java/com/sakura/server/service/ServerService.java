package com.sakura.server.service;

/**
 * Server服务接口
 */
public interface ServerService {
    
    /**
     * 处理请求
     * @param request 请求内容
     * @return 响应结果
     */
    String handleRequest(String request);
}
