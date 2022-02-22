package com.nowcoder.community.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Alex
 * @version 1.0
 * @company xxx
 * @copyright (c)  xxxInc. All rights reserved.
 * @date 2022/2/21 15:25
 */
@Service
@Slf4j
public class TaskService {
    /**
     * executeTask() 方法在多线程环境下被自动异步调用
     */
    @Async
    public void executeTask(){
        log.info("execute task......" + System.currentTimeMillis());
    }

    /**
     * executeScheduledTask() 任务定时执行 自动异步调用
     */
    // @Scheduled(initialDelay = 10000,fixedRate = 1000)
    public void executeScheduledTask(){
        log.info("execute scheduled task......" + System.currentTimeMillis());
    }
}
