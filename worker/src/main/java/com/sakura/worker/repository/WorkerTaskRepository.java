package com.sakura.worker.repository;

import com.sakura.worker.entity.WorkerTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Worker任务Repository
 */
@Repository
public interface WorkerTaskRepository extends JpaRepository<WorkerTask, Long> {
    // 自定义查询方法
}
