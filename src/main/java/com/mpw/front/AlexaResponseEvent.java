package com.mpw.front;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Created by wilk.wojtek@gmail.com.
 */
public class AlexaResponseEvent {
    private final UUID requestId;
    private final String payload;

    @JsonCreator
    public AlexaResponseEvent(@JsonProperty("requestId") UUID requestId,
                              @JsonProperty("payload") String payload) {
        this.requestId = requestId;
        this.payload = payload;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public String getPayload() {
        return payload;
    }
}
