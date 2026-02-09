package com.sakura.worker.service.impl;

import com.sakura.worker.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 任务服务实现类，负责调用Server端的接口
 */
@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${server-url.base-url}")
    private String serverBaseUrl;

    private static final String PIC_TASK_KEY = "PIC_TASK";

    /**
     * 通过调用server端的接口获取PIC_TASK的JSON数组第一个元素
     * @return 第一个元素的内容，获取失败返回null
     */
    @Override
    public Object getFirstTask() {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(serverBaseUrl)
                    .path("/api/redis/getFirstElement")
                    .queryParam("key", PIC_TASK_KEY)
                    .toUriString();
            ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
            return response.getBody();
        } catch (Exception e) {
            logger.error("获取任务失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 调用server端接口，删除已完成的任务（删除JSON数组的第一个元素）
     * @return 删除结果
     */
    @Override
    public String removeCompletedTask() {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(serverBaseUrl)
                    .path("/api/redis/removeFirstElement")
                    .queryParam("key", PIC_TASK_KEY)
                    .toUriString();
            ResponseEntity<String> response = restTemplate.exchange(url, 
                org.springframework.http.HttpMethod.DELETE, 
                null, 
                String.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("删除已完成任务失败: " + e.getMessage(), e);
        }
    }
}
