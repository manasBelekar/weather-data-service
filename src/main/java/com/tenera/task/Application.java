package com.tenera.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@EntityScan("com.tenera.model")
@ComponentScan({ "com.tenera.controller", "com.tenera.service", "com.tenera.task", "com.tenera.dao", "com.tenera.mapper", "com.tenera.common" })
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}	

}
