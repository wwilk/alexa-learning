package com.mpw.front;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Created by wilk.wojtek@gmail.com.
 */
public class AlexaResponseEvent {
    private final UUID requestId;
    private final String message;

    @JsonCreator
    public AlexaResponseEvent(@JsonProperty("requestId") UUID requestId,
                              @JsonProperty("message") String message) {
        this.requestId = requestId;
        this.message = message;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public String getMessage() {
        return message;
    }
}
