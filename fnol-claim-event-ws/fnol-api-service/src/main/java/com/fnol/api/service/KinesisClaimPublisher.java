package com.fnol.api.service;

import com.fnol.common.ClaimEventEnvelope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class KinesisClaimPublisher {

    private final KinesisClient kinesisClient;

    public void publish(ClaimEventEnvelope<?> event, String streamName) {
        String payload = event.eventType() + "|" + event.claimId() + "|" + event.timestamp();
        PutRecordRequest request = PutRecordRequest.builder()
                .streamName(streamName)
                .partitionKey(event.claimId())
                .data(SdkBytes.fromString(payload, StandardCharsets.UTF_8))
                .build();

        kinesisClient.putRecord(request);
    }
}
