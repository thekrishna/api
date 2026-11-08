package com.eventplatform.sqs;

import com.enterprise.event.core.dlq.DeadLetterRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SqsDeadLetterQueueTest {

    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    @Test
    void shouldPublishFailedEventToSqsQueue() {
        SqsClient client = mock(SqsClient.class);
        when(client.sendMessage(any(SendMessageRequest.class))).thenReturn(software.amazon.awssdk.services.sqs.model.SendMessageResponse.builder().messageId("msg-1").build());

        SqsDeadLetterQueue queue = new SqsDeadLetterQueue(client, "https://example.local/queue/dlq");
        DeadLetterRecord record = new DeadLetterRecord("evt-42", "user.created", "E001", "invalid payload", Instant.now(), "{\"id\":42}", "corr-42", "IllegalArgumentException", "bad input");

        queue.enqueue(record);

        verify(client).sendMessage(any(SendMessageRequest.class));
    }

    @Test
    void shouldReceiveAndDeleteMessage() throws Exception {
        SqsClient client = mock(SqsClient.class);
        DeadLetterRecord record = new DeadLetterRecord("evt-88", "user.updated", "E002", "processing failure", Instant.now(), "{\"status\":\"failed\"}", "corr-88", "RuntimeException", "boom");
        String body = mapper.writeValueAsString(record);
        when(client.receiveMessage(any(ReceiveMessageRequest.class))).thenReturn(ReceiveMessageResponse.builder().messages(
                Message.builder().body(body).receiptHandle("receipt-88").messageId("msg-88").build()
        ).build());

        SqsDeadLetterQueue queue = new SqsDeadLetterQueue(client, "https://example.local/queue/dlq");

        Optional<DeadLetterRecord> maybeRecord = queue.receive();

        assertTrue(maybeRecord.isPresent());
        assertEquals("evt-88", maybeRecord.orElseThrow().eventId());
        verify(client).deleteMessage(any(DeleteMessageRequest.class));
    }

    @Test
    void shouldFailFastWhenSqsCommunicationBreaks() {
        SqsClient client = mock(SqsClient.class);
        when(client.sendMessage(any(SendMessageRequest.class)))
                .thenThrow(SqsException.builder().message("service unavailable").statusCode(503).build());

        SqsDeadLetterQueue queue = new SqsDeadLetterQueue(client, "https://example.local/queue/dlq");
        DeadLetterRecord record = new DeadLetterRecord("evt-99", "order.created", "E500", "downstream error", Instant.now());

        assertThrows(IllegalStateException.class, () -> queue.enqueue(record));
    }
}
