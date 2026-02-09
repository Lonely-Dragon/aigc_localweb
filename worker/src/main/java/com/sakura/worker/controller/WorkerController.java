package com.sakura.worker.controller;

import com.sakura.worker.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Worker控制器
 */
@RestController
@RequestMapping("/api/worker")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    @PostMapping("/task")
    public String processTask(@RequestBody String task) {
        return workerService.processTask(task);
    }

    @GetMapping("/status")
    public String getStatus() {
        return "Worker服务运行中";
    }
}
