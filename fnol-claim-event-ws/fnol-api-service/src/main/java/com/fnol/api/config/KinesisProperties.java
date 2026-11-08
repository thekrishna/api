package com.fnol.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws.kinesis")
public class KinesisProperties {

    private String fnolClaimEventsStream = "fnol-claim-events";

    public String getFnolClaimEventsStream() {
        return fnolClaimEventsStream;
    }

    public void setFnolClaimEventsStream(String fnolClaimEventsStream) {
        this.fnolClaimEventsStream = fnolClaimEventsStream;
    }
}
