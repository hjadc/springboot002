package com.huju.task.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 测试异步任务
 * Created by huju on 2018/11/18.
 */

@Service
public class AsyncService {

    /**
     * 打上@Async注解,告诉程序,这个方法是一个异步线程
     */
    @Async
    public void hello(){

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("数据处理中...");
    }
}
