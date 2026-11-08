package com.eventplatform.dynamodb;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DynamoDbConfigTest {

    @Test
    void shouldCreateDynamoDbClientBean() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.getEnvironment().getPropertySources().addFirst(new MapPropertySource("test-properties", Map.of(
                    "aws.region", "us-east-1"
            )));
            context.register(DynamoDbConfig.class);
            context.refresh();

            assertNotNull(context.getBean(DynamoDbClient.class));
        }
    }
}
