package com.huju;

import com.huju.cache.dao.EmployeeMapper;
import com.huju.cache.entities.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

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

}
