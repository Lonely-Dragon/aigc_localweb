package com.sakura.server.controller;

import com.sakura.server.service.RedisService;
import com.sakura.server.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Redis测试控制器
 */
@RestController
@RequestMapping("/api/redis")
public class RedisController {

    @Autowired
    private RedisService redisService;

    /**
     * 获取JSON数组的第一个元素
     * @param key 键
     * @return 第一个元素，如果不存在或数组为空则返回null
     */
    @GetMapping("/getFirstElement")
    public Object getFirstElement(@RequestParam String key) {
        Object element = redisService.getFirstElementFromJsonArray(key);
        return element != null ? element : "键不存在或数组为空";
    }

    /**
     * 删除JSON数组的第一个元素
     * @param key 键
     * @return 删除结果
     */
    @DeleteMapping("/removeFirstElement")
    public String removeFirstElement(@RequestParam String key) {
        Boolean result = redisService.removeFirstElementFromJsonArray(key);
        return result ? "删除成功" : "键不存在或数组为空";
    }
}
