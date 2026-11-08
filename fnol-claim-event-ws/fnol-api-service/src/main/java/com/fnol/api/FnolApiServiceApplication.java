package com.fnol.api;

import com.fnol.api.config.KinesisProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(KinesisProperties.class)
public class FnolApiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FnolApiServiceApplication.class, args);
    }
}
