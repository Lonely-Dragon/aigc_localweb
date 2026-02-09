package com.sakura.server.controller;

import com.sakura.server.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Server控制器
 */
@RestController
@RequestMapping("/api/server")
public class ServerController {

    @Autowired
    private ServerService serverService;

    @PostMapping("/request")
    public String handleRequest(@RequestBody String request) {
        return serverService.handleRequest(request);
    }

    @GetMapping("/health")
    public String health() {
        return "Server服务运行正常";
    }
}
