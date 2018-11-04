package com.huju;

import com.huju.cache.dao.EmployeeMapper;
import com.huju.cache.entities.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot002ApplicationTests {

	@Autowired
	EmployeeMapper employeeMapper;

	@Test
	public void contextLoads() {
		Employee empById = employeeMapper.getEmpById(1);
		System.out.println(empById);
	}

}
