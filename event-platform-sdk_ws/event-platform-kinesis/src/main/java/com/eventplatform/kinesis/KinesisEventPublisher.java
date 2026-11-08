package com.eventplatform.kinesis;

import com.eventplatform.model.event.GenericEventEnvelope;
import com.eventplatform.producer.EventProducer;
import software.amazon.awssdk.services.kinesis.KinesisClient;

import java.util.Objects;

public class KinesisEventPublisher<T> implements EventProducer<T> {

    private final KinesisClient kinesisClient;
    private final String streamName;

    public KinesisEventPublisher(KinesisClient kinesisClient, String streamName) {
        this.kinesisClient = Objects.requireNonNull(kinesisClient, "kinesisClient must not be null");
        this.streamName = Objects.requireNonNull(streamName, "streamName must not be null");
    }

    @Override
    public void publish(GenericEventEnvelope<T> envelope) {
        Objects.requireNonNull(envelope, "envelope must not be null");
        kinesisClient.putRecord(builder -> builder.streamName(streamName)
                .partitionKey(envelope.getEventId().value())
                .data(software.amazon.awssdk.core.SdkBytes.fromUtf8String(envelope.getEventType())));
    }
}
