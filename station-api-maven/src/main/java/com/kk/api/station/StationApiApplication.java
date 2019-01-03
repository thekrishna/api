package com.kk.api.station;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.kk.api.station"})
public class StationApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StationApiApplication.class, args);
	}

}

