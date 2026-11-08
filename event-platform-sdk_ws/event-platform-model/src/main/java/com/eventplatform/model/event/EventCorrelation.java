package com.eventplatform.model.event;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
public class EventCorrelation implements Serializable {

    private final String correlationId;
    private final String causationId;
    private final String parentEventId;
    private final String conversationId;
    private final Map<String, String> attributes;
}
