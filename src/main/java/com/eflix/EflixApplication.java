package com.eflix;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.eflix.**.mapper")
public class EflixApplication {

	public static void main(String[] args) {
		SpringApplication.run(EflixApplication.class, args);
	}

}
