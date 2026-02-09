package com.sakura.worker.service;

import com.sakura.worker.dto.Task;
import com.sakura.worker.enums.TaskType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 任务处理器服务
 * 根据任务类型路由到对应的处理器
 */
@Service
public class TaskHandlerService {

    private static final Logger logger = LoggerFactory.getLogger(TaskHandlerService.class);

    /**
     * 所有任务处理器的映射
     */
    private final Map<TaskType, TaskHandler> taskHandlerMap;

    /**
     * 构造函数，自动注入所有TaskHandler实现类
     */
    @Autowired
    public TaskHandlerService(List<TaskHandler> taskHandlers) {
        // 将所有TaskHandler实现类按照支持的任务类型进行映射
        // 将String类型的任务类型代码转换为TaskType枚举作为key
        this.taskHandlerMap = taskHandlers.stream()
                .collect(Collectors.toMap(
                        handler -> {
                            String code = handler.getSupportedTaskType();
                            TaskType type = TaskType.fromCode(code);
                            if (type == null) {
                                throw new IllegalStateException("无效的任务类型代码: " + code);
                            }
                            return type;
                        },
                        handler -> handler,
                        (existing, replacement) -> {
                            logger.warn("发现重复的任务处理器: {}, 使用后注册的处理器", existing.getSupportedTaskType());
                            return replacement;
                        }
                ));
        
        logger.info("已注册任务处理器: {}", taskHandlerMap.keySet());
    }

    /**
     * 处理任务
     * @param task 任务对象
     * @return 处理结果
     */
    public String processTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("任务不能为空");
        }

        TaskType taskType = task.getTaskTypeEnum();
        if (taskType == null) {
            throw new IllegalArgumentException("不支持的任务类型: " + task.getTaskType());
        }

        TaskHandler handler = taskHandlerMap.get(taskType);
        if (handler == null) {
            throw new IllegalStateException("未找到任务类型 " + taskType + " 对应的处理器");
        }

        logger.info("使用处理器 {} 处理任务类型: {}", handler.getClass().getSimpleName(), taskType);
        return handler.handle(task);
    }
}
