package com.axity.curso.retailservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class RetailserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetailserviceApplication.class, args);
	}

}
