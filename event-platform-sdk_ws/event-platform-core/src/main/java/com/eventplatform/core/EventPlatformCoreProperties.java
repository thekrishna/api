package com.eventplatform.core;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Builder(toBuilder = true)
@ToString
public class EventPlatformCoreProperties implements Serializable {

    private final String defaultEventVersion;
    private final String defaultSource;
    private final int maxRetryAttempts;
    private final long retryDelayMs;
    private final int maxBatchSize;

    public EventPlatformCoreProperties() {
        this("v1", "event-platform-core", 5, 1000L, 100);
    }

    public EventPlatformCoreProperties(
            String defaultEventVersion,
            String defaultSource,
            int maxRetryAttempts,
            long retryDelayMs,
            int maxBatchSize
    ) {
        this.defaultEventVersion = defaultEventVersion;
        this.defaultSource = defaultSource;
        this.maxRetryAttempts = maxRetryAttempts;
        this.retryDelayMs = retryDelayMs;
        this.maxBatchSize = maxBatchSize;
    }
}
