package com.eventplatform.model.event;

public enum EventStatus {
    NEW,
    PUBLISHED,
    RECEIVED,
    CONSUMED,
    PROCESSED,
    FAILED,
    PENDING_RETRY,
    DLQ,
    REPLAYED,
    ARCHIVED
}
