package com.sakura.server.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Server服务测试类
 */
@SpringBootTest
public class ServerServiceTest {

    @Autowired
    private ServerService serverService;

    @Test
    public void testHandleRequest() {
        String result = serverService.handleRequest("测试请求");
        assertEquals("Worker处理任务: 测试请求", result);
    }
}
