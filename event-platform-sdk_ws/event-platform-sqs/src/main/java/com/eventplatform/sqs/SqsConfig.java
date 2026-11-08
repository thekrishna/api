package com.eventplatform.sqs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.time.Duration;

@Configuration
public class SqsConfig {

    @Bean
    public SqsClient sqsClient(@Value("${aws.region:us-east-1}") String region) {
        return SqsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean
    public SqsDeadLetterQueue sqsDeadLetterQueue(
            SqsClient sqsClient,
            @Value("${eventplatform.sqs.queue-url:}") String queueUrl,
            @Value("${eventplatform.sqs.visibility-timeout-seconds:30}") long visibilityTimeoutSeconds,
            @Value("${eventplatform.sqs.max-receive-count:5}") int maxReceiveCount) {
        if (queueUrl == null || queueUrl.isBlank()) {
            throw new IllegalStateException("eventplatform.sqs.queue-url must be configured");
        }
        return new SqsDeadLetterQueue(sqsClient, queueUrl, Duration.ofSeconds(visibilityTimeoutSeconds), maxReceiveCount);
    }
}
