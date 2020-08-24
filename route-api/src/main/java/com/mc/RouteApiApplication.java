package com.mc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.mc"})
public class RouteApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(RouteApiApplication.class, args);
	}

}