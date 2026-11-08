package com.eventplatform.kinesis;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;
import software.amazon.awssdk.services.kinesis.KinesisClient;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class KinesisConfigTest {

    @Test
    void shouldCreateKinesisClientBean() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.getEnvironment().getPropertySources().addFirst(new MapPropertySource("test-properties", Map.of(
                    "aws.region", "us-east-1"
            )));
            context.register(KinesisConfig.class);
            context.refresh();

            assertNotNull(context.getBean(KinesisClient.class));
        }
    }
}
