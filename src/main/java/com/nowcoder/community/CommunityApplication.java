package com.nowcoder.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

/**
 * 项目启动类
 * @author Alex
 * @version 1.0
 * @date 2022/2/1 15:15
 */
@SpringBootApplication
public class CommunityApplication {

    @PostConstruct
    public void init(){
        // 解决es和redis依赖netty的冲突问题
        // 见源码Netty2Utils.setAvailableProcessor()
        System.setProperty("es.set.netty.runtime.available.processors","false");
    }

    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class,args);
    }

}
