package com.eventplatform.sqs;

import com.enterprise.event.core.dlq.DeadLetterQueue;
import com.enterprise.event.core.dlq.DeadLetterRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class SqsDeadLetterQueue implements DeadLetterQueue {

    private final SqsClient sqsClient;
    private final String queueUrl;
    private final ObjectMapper objectMapper;
    private final Duration visibilityTimeout;
    private final int maxReceiveCount;

    public SqsDeadLetterQueue(SqsClient sqsClient, String queueUrl) {
        this(sqsClient, queueUrl, Duration.ofSeconds(30), 5);
    }

    public SqsDeadLetterQueue(SqsClient sqsClient, String queueUrl, Duration visibilityTimeout) {
        this(sqsClient, queueUrl, visibilityTimeout, 5);
    }

    public SqsDeadLetterQueue(SqsClient sqsClient, String queueUrl, Duration visibilityTimeout, int maxReceiveCount) {
        this.sqsClient = Objects.requireNonNull(sqsClient, "sqsClient must not be null");
        this.queueUrl = validateQueueUrl(queueUrl);
        this.visibilityTimeout = visibilityTimeout == null || visibilityTimeout.isZero() || visibilityTimeout.isNegative()
                ? Duration.ofSeconds(30)
                : visibilityTimeout;
        this.maxReceiveCount = maxReceiveCount > 0 ? maxReceiveCount : 5;
        this.objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
    }

    @Override
    public void enqueue(DeadLetterRecord record) {
        Objects.requireNonNull(record, "record must not be null");
        try {
            String body = objectMapper.writeValueAsString(record);
            sqsClient.sendMessage(SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(body)
                    .messageAttributes(messageAttributes(record))
                    .build());
        } catch (JsonProcessingException | SqsException e) {
            throw new IllegalStateException("Failed to enqueue dead-letter message for event " + record.eventId() + " to SQS queue " + queueUrl, e);
        }
    }

    @Override
    public Optional<DeadLetterRecord> find(String eventId) {
        Objects.requireNonNull(eventId, "eventId must not be null");
        try {
            for (Message message : receiveMessages(10).messages()) {
                DeadLetterRecord record = deserialize(message.body());
                if (eventId.equals(record.eventId())) {
                    return Optional.of(record);
                }
            }
        } catch (SqsException e) {
            throw new IllegalStateException("Failed to read dead-letter messages from SQS queue " + queueUrl, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<DeadLetterRecord> receive() {
        try {
            List<Message> messages = receiveMessages(1).messages();
            if (messages.isEmpty()) {
                return Optional.empty();
            }
            Message message = messages.getFirst();
            DeadLetterRecord record = deserialize(message.body());
            sqsClient.deleteMessage(DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(message.receiptHandle())
                    .build());
            return Optional.of(record);
        } catch (SqsException e) {
            throw new IllegalStateException("Failed to receive dead-letter message from SQS queue " + queueUrl, e);
        }
    }

    @Override
    public boolean delete(String eventId) {
        Objects.requireNonNull(eventId, "eventId must not be null");
        try {
            for (Message message : receiveMessages(10).messages()) {
                DeadLetterRecord record = deserialize(message.body());
                if (eventId.equals(record.eventId())) {
                    sqsClient.deleteMessage(DeleteMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .receiptHandle(message.receiptHandle())
                            .build());
                    return true;
                }
            }
            return false;
        } catch (SqsException | IllegalArgumentException e) {
            throw new IllegalStateException("Failed to delete dead-letter message for event " + eventId + " from SQS queue " + queueUrl, e);
        }
    }

    @Override
    public List<DeadLetterRecord> all() {
        try {
            List<DeadLetterRecord> allMessages = new ArrayList<>();
            for (Message message : receiveMessages(10).messages()) {
                allMessages.add(deserialize(message.body()));
            }
            return allMessages;
        } catch (SqsException e) {
            throw new IllegalStateException("Failed to list dead-letter messages from SQS queue " + queueUrl, e);
        }
    }

    public Duration getVisibilityTimeout() {
        return visibilityTimeout;
    }

    public int getMaxReceiveCount() {
        return maxReceiveCount;
    }

    private ReceiveMessageResponse receiveMessages(int maxNumberOfMessages) {
        return sqsClient.receiveMessage(ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(Math.min(maxNumberOfMessages, 10))
                .waitTimeSeconds(1)
                .visibilityTimeout((int) visibilityTimeout.getSeconds())
                .build());
    }

    private DeadLetterRecord deserialize(String body) {
        try {
            return objectMapper.readValue(body, DeadLetterRecord.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to deserialize SQS dead-letter message body from queue " + queueUrl, e);
        }
    }

    private Map<String, MessageAttributeValue> messageAttributes(DeadLetterRecord record) {
        Map<String, MessageAttributeValue> attributes = new LinkedHashMap<>();
        attributes.put("eventId", MessageAttributeValue.builder().dataType("String").stringValue(record.eventId()).build());
        attributes.put("eventType", MessageAttributeValue.builder().dataType("String").stringValue(record.eventType()).build());
        if (record.correlationId() != null && !record.correlationId().isBlank()) {
            attributes.put("correlationId", MessageAttributeValue.builder().dataType("String").stringValue(record.correlationId()).build());
        }
        return attributes;
    }

    private static String validateQueueUrl(String queueUrl) {
        String value = Objects.requireNonNull(queueUrl, "queueUrl must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("queueUrl must not be blank");
        }
        return value;
    }
}
