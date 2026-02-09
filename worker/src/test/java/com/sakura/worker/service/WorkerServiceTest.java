package com.sakura.worker.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Worker服务测试类
 */
@SpringBootTest
public class WorkerServiceTest {

    @Autowired
    private WorkerService workerService;

    @Test
    public void testProcessTask() {
        String result = workerService.processTask("测试任务");
        assertEquals("Worker处理任务: 测试任务", result);
    }

    @Test
    public void testStartAndStop() {
        assertDoesNotThrow(() -> {
            workerService.start();
            workerService.stop();
        });
    }
}
