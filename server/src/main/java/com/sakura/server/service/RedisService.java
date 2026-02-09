package com.sakura.server.service;

/**
 * Redis服务接口
 */
public interface RedisService {

    /**
     * 获取JSON数组的第一个元素
     * @param key 键
     * @return 第一个元素，如果不存在或数组为空则返回null
     */
    Object getFirstElementFromJsonArray(String key);

    /**
     * 删除JSON数组的第一个元素
     * @param key 键
     * @return 是否删除成功
     */
    Boolean removeFirstElementFromJsonArray(String key);

    /**
     * 向JSON数组添加元素（添加到末尾）
     * @param key 键
     * @param element 要添加的元素
     * @return 是否添加成功
     */
    Boolean addElementToJsonArray(String key, Object element);
}
