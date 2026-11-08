package com.enterprise.event.core.retry;

import java.io.Serializable;

public record RetryPolicy(
        int maxAttempts,
        long initialDelayMs,
        long maxDelayMs,
        double multiplier
) implements Serializable {
    public static RetryPolicy defaults() {
        return new RetryPolicy(5, 1000L, 30000L, 2.0d);
    }
}
