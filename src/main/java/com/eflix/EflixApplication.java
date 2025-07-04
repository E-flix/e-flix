package com.eflix;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

// @MapperScan("com.eflix.**.mapper")
@SpringBootApplication
@MapperScan(basePackages = "com.eflix.**.mapper")
@EnableScheduling // 0704
public class EflixApplication {

	public static void main(String[] args) {
		SpringApplication.run(EflixApplication.class, args);
	}

}
