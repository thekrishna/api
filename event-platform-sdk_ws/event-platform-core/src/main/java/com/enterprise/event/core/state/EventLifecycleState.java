package com.enterprise.event.core.state;

public enum EventLifecycleState {
    NEW,
    PUBLISHED,
    PROCESSING,
    PENDING_RETRY,
    DLQ,
    CONSUMED,
    REJECTED
}
