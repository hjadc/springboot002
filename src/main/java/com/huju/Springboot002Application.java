package com.huju;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(value = "com.huju.*.dao")
@SpringBootApplication
public class Springboot002Application {

	public static void main(String[] args) {
		SpringApplication.run(Springboot002Application.class, args);
	}
}
