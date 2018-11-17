package com.huju;

import com.huju.cache.dao.EmployeeMapper;
import com.huju.cache.entities.Employee;
import com.huju.rabbitmq.entities.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot002ApplicationTests {

	@Autowired
	EmployeeMapper employeeMapper;
	@Autowired
	RedisTemplate redisTemplate;

	@Test
	public void contextLoads() {
		Employee empById = employeeMapper.getEmpById(1);
		System.out.println(empById);
	}

	@Test
	public void redisTest() {
		Employee empById = employeeMapper.getEmpById(1);
		redisTemplate.opsForValue().set("emp",empById);

		Object o = redisTemplate.opsForValue().get("emp::2");
		System.out.println(o);
	}

	// ***************************** RabbitMq ********************************

	@Autowired
	RabbitTemplate rabbitTemplate;

	/**
	 * 单播 (点对点)
	 */
	@Test
	public void send() {

		/**
		 * Message需要自己构造一个;定义消息体内容和消息头
		 *
		 * exchage 交换器
		 * routeKey 队列key
		 * message  消息对象
		 *
		 * rabbitTemplate.send(exchage,routeKey,message);
		 */

		// 常用的是这种.只需要传入要发送的对象,自动序列化发送给rabbitmq,object默认当成消息体
		Map<String, Object> map = new HashMap<>();
		map.put("msg", "嗨!你好,这是第一个消息!");
		map.put("data", Arrays.asList("你好呀!", 666888, true));

		// 对象被默认序列化(JDK机制)以后发送出去
		rabbitTemplate.convertAndSend("exchange.direct", "test.news", new Book("红楼梦","曹雪芹"));
	}

	/**
	 * 接收消息
	 */
	@Test
	public void receive(){
		// 该方法是接收并转换,参数是取哪个队列里的
		Object o = rabbitTemplate.receiveAndConvert("test.news");
		System.out.println("********* 接收的对象类型是:" + o.getClass());
		System.out.println("********* 接收的数据是:" +o);
	}

	/**
	 * 广播
	 */
	@Test
	public void sendMsg(){
		// exchange.fanout是广播交换器,所有不用指定消息队列
		rabbitTemplate.convertAndSend("exchange.fanout", "", new Book("三国演义","罗贯中"));

	}

}
