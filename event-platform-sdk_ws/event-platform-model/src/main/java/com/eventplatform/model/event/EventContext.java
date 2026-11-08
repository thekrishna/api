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
public class EventContext implements Serializable {

    private final String tenantId;
    private final String traceId;
    private final String spanId;
    private final String userId;
    private final String region;
    private final Map<String, String> attributes;
}
