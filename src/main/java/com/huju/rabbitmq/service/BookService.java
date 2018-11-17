package com.huju.rabbitmq.service;

import com.alibaba.fastjson.JSONObject;
import com.huju.rabbitmq.entities.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Created by huju on 2018/11/17.
 */

@Slf4j
@Service
public class BookService {

    /**
     * @RabbitListener 打开监听该方法,当消息队列"test.news"里有消息后,会立马执行该方法
     * queues 指定哪个队列,数组形式,可以监听多个队列
     */
    @RabbitListener(queues = {"test.news"})
    public void receive(Book book) {
        log.info("************ 收到消息,内容: {} ***********", JSONObject.toJSONString(book));
    }

    @RabbitListener(queues = {"test"})
    public void receive(Message message){
        log.info("************ 收到消息,内容: {} ***********", JSONObject.toJSONString(message.getBody()));
        log.info("************ 收到消息,头信息: {} ***********", JSONObject.toJSONString(message.getMessageProperties()));
    }
}
