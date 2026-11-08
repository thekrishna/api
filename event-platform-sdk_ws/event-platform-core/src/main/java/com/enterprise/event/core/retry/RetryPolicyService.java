package com.enterprise.event.core.retry;

public class RetryPolicyService {

    private final RetryPolicy retryPolicy;

    public RetryPolicyService() {
        this(RetryPolicy.defaults());
    }

    public RetryPolicyService(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy == null ? RetryPolicy.defaults() : retryPolicy;
    }

    public RetryDecision decide(int retryCount) {
        if (retryCount < retryPolicy.maxAttempts()) {
            return RetryDecision.RETRY;
        }
        return RetryDecision.DLQ;
    }
}
