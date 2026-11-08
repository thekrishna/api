package com.enterprise.event.core.aggregation;

import java.io.Serializable;
import java.util.Map;

public record EventAggregationResult(
        String correlationId,
        Map<String, Object> aggregates
) implements Serializable {
}
