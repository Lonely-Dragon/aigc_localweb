package com.sakura.server.controller;

import com.sakura.server.service.RedisService;
import com.sakura.worker.dto.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 任务控制器
 * 提供任务推送等接口
 */
@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private RedisService redisService;

    /**
     * 任务推送接口
     * 将任务添加到Redis的任务队列中
     * @param task 任务对象，包含taskType和params
     * @return 推送结果
     */
    @PostMapping("/push")
    public ResponseEntity<?> pushTask(@RequestBody Task task) {
        try {
            // 参数验证
            if (task == null) {
                return ResponseEntity.badRequest().body("任务对象不能为空");
            }
            if (task.getTaskType() == null || task.getTaskType().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("任务类型不能为空");
            }
            
            // 将任务添加到Redis的TASK队列中
            Boolean result = redisService.addElementToJsonArray("TASK", task);
            
            if (result) {
                return ResponseEntity.ok().body("任务推送成功");
            } else {
                return ResponseEntity.internalServerError().body("任务推送失败");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("任务推送异常: " + e.getMessage());
        }
    }

    /**
     * 批量任务推送接口
     * 将多个任务添加到Redis的任务队列中
     * @param tasks 任务对象数组
     * @return 推送结果
     */
    @PostMapping("/pushBatch")
    public ResponseEntity<?> pushTasks(@RequestBody Task[] tasks) {
        try {
            // 参数验证
            if (tasks == null || tasks.length == 0) {
                return ResponseEntity.badRequest().body("任务列表不能为空");
            }
            
            int successCount = 0;
            int failCount = 0;
            
            for (Task task : tasks) {
                if (task == null || task.getTaskType() == null || task.getTaskType().trim().isEmpty()) {
                    failCount++;
                    continue;
                }
                
                try {
                    Boolean result = redisService.addElementToJsonArray("TASK", task);
                    if (result) {
                        successCount++;
                    } else {
                        failCount++;
                    }
                } catch (Exception e) {
                    failCount++;
                }
            }
            
            return ResponseEntity.ok().body(String.format("批量推送完成: 成功 %d 个, 失败 %d 个", successCount, failCount));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("批量任务推送异常: " + e.getMessage());
        }
    }
}
