package com.enterprise.event.core.validator;

public interface EventValidator<T> {

    void validate(T payload);
}
