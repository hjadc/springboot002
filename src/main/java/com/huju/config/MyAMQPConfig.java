package com.huju.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by huju on 2018/11/17.
 */

@Configuration
public class MyAMQPConfig {

    /**
     * 修改RabbitMQ的默认序列化为json,原来的是JDK机制
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {

        return new Jackson2JsonMessageConverter();
    }
}
