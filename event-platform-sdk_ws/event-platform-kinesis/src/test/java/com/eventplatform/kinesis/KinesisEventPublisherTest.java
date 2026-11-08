package com.eventplatform.kinesis;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.kinesis.KinesisClient;

import static org.junit.jupiter.api.Assertions.*;

class KinesisEventPublisherTest {

    @Test
    void shouldCreatePublisher() {
        KinesisClient client = KinesisClient.builder().region(software.amazon.awssdk.regions.Region.US_EAST_1).credentialsProvider(
                software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider.create()).build();

        KinesisEventPublisher<String> publisher = new KinesisEventPublisher<>(client, "test-stream");
        assertNotNull(publisher);
        client.close();
    }
}
