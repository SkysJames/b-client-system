package com.lx.cus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "com.lx.cus.entity")
@ServletComponentScan
public class ApplicationRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(ApplicationRunner.class, args);
	}

}
