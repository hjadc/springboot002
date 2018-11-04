package com.huju;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 快速体验缓存
 * 1.创建项目的时候勾选 cache
 * 2.开启基于注解 @EnableCaching
 * 2.标注缓存注解即可
 * 		@Cacheable		主要针对方法配置，能够根据方法的请求参数对其结果进行缓存
 * 		@CacheEvict		清空缓存
 * 		@CachePut		保证方法被调用，又希望结果被缓存。
 */
@MapperScan(value = "com.huju.*.dao")
@SpringBootApplication
@EnableCaching
public class Springboot002Application {

	public static void main(String[] args) {
		SpringApplication.run(Springboot002Application.class, args);
	}
}
