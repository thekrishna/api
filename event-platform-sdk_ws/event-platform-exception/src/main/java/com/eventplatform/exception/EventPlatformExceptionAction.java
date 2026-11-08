package com.eventplatform.exception;

public enum EventPlatformExceptionAction {
    RETRY,
    DLQ,
    PENDING,
    IGNORE,
    FAIL,
    REPLAY
}
