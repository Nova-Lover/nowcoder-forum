package com.nowcoder.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 线程池配置
 * @author Alex
 * @version 1.0
 * @company xxx
 * @copyright (c)  xxxInc. All rights reserved.
 * @date 2022/2/21 14:55
 */
@Configuration
@EnableScheduling // 启用spring定时任务
@EnableAsync // 启用线程异步执行任务
public class ThreadPoolConfig {
}
