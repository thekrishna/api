package com.eventplatform.model.event;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
public class EventContract implements Serializable {

    private final String contractName;
    private final String version;
    private final String schema;
    private final String description;
    private final boolean valid;
}
