package com.eventplatform.kinesis;

import java.io.Serializable;

public record KinesisProperties(
        String streamName,
        String region,
        boolean enabled
) implements Serializable {
}
