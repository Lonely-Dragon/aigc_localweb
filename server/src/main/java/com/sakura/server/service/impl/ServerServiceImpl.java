package com.sakura.server.service.impl;

import com.sakura.server.service.ServerService;
import com.sakura.worker.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Server服务实现类
 */
@Service
public class ServerServiceImpl implements ServerService {
    
    @Autowired
    private WorkerService workerService;

    /**
     * 处理请求
     * @param request 请求内容
     * @return 响应结果
     */
    @Override
    public String handleRequest(String request) {
        return workerService.processTask(request);
    }
}
