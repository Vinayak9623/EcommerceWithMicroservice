package com.micro.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServicesApplication.class, args);

		System.out.println("Welcome to Order Service");
	}

}
