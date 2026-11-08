package com.enterprise.event.core.retry;

public enum RetryDecision {
    RETRY,
    DLQ,
    IGNORE
}
