package com.kk.api.station;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * @author Krishna Kumar
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.kk.api.station"})
@EnableSwagger2
public class StationApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StationApiApplication.class, args);
	}

}

