package com.eventplatform.sqs;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SqsConfigTest {

    @Test
    void shouldCreateSqsBeansFromApplicationProperties() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.getEnvironment().getPropertySources().addFirst(new MapPropertySource("test-properties", Map.of(
                    "aws.region", "us-east-1",
                    "eventplatform.sqs.queue-url", "https://sqs.us-east-1.amazonaws.com/123456789012/test-dlq",
                    "eventplatform.sqs.visibility-timeout-seconds", "30",
                    "eventplatform.sqs.max-receive-count", "5"
            )));
            context.register(SqsConfig.class);
            context.refresh();

            assertNotNull(context.getBean(SqsClient.class));
            assertNotNull(context.getBean(SqsDeadLetterQueue.class));
        }
    }
}
