package com.sakura.server.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sakura.server.service.RedisService;
import com.sakura.server.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Redis服务实现类
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取JSON数组的第一个元素
     * @param key 键
     * @return 第一个元素，如果不存在或数组为空则返回null
     */
    @Override
    public Object getFirstElementFromJsonArray(String key) {
        Object value = redisUtils.get(key);
        if (value == null) {
            return null;
        }
        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Object> jsonArray;
            
            // 如果value是字符串，需要解析
            if (value instanceof String) {
                jsonArray = objectMapper.readValue((String) value, new TypeReference<List<Object>>() {});
            } else if (value instanceof List) {
                // 如果已经是List类型，直接使用
                jsonArray = (List<Object>) value;
            } else {
                // 尝试转换为List
                jsonArray = objectMapper.convertValue(value, new TypeReference<List<Object>>() {});
            }
            
            if (jsonArray != null && !jsonArray.isEmpty()) {
                return jsonArray.get(0);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("解析JSON数组失败: " + e.getMessage(), e);
        }
    }

    /**
     * 删除JSON数组的第一个元素
     * @param key 键
     * @return 是否删除成功
     */
    @Override
    public Boolean removeFirstElementFromJsonArray(String key) {
        Object value = redisUtils.get(key);
        if (value == null) {
            return false;
        }
        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Object> jsonArray;
            
            // 如果value是字符串，需要解析
            if (value instanceof String) {
                jsonArray = objectMapper.readValue((String) value, new TypeReference<List<Object>>() {});
            } else if (value instanceof List) {
                // 如果已经是List类型，直接使用
                jsonArray = new ArrayList<>((List<Object>) value);
            } else {
                // 尝试转换为List
                jsonArray = new ArrayList<>(objectMapper.convertValue(value, new TypeReference<List<Object>>() {}));
            }
            
            if (jsonArray != null && !jsonArray.isEmpty()) {
                jsonArray.remove(0);
                // 将更新后的数组写回Redis
                redisUtils.set(key, jsonArray);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("删除JSON数组第一个元素失败: " + e.getMessage(), e);
        }
    }

    /**
     * 向JSON数组添加元素（添加到末尾）
     * @param key 键
     * @param element 要添加的元素
     * @return 是否添加成功
     */
    @Override
    public Boolean addElementToJsonArray(String key, Object element) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Object> jsonArray;
            
            Object value = redisUtils.get(key);
            if (value == null) {
                // 如果key不存在，创建新的数组
                jsonArray = new ArrayList<>();
            } else {
                // 如果value是字符串，需要解析
                if (value instanceof String) {
                    jsonArray = objectMapper.readValue((String) value, new TypeReference<List<Object>>() {});
                } else if (value instanceof List) {
                    // 如果已经是List类型，直接使用
                    jsonArray = new ArrayList<>((List<Object>) value);
                } else {
                    // 尝试转换为List
                    jsonArray = new ArrayList<>(objectMapper.convertValue(value, new TypeReference<List<Object>>() {}));
                }
            }
            
            // 添加元素到数组末尾
            jsonArray.add(element);
            
            // 将更新后的数组写回Redis
            redisUtils.set(key, jsonArray);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("向JSON数组添加元素失败: " + e.getMessage(), e);
        }
    }
}
