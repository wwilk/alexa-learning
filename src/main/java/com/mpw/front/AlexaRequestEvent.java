package com.mpw.front;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Created by wilk.wojtek@gmail.com.
 */
public class AlexaRequestEvent {

    @JsonProperty
    private UUID eventId;
    @JsonProperty
    private Method method;

    public AlexaRequestEvent(){

    }

    public AlexaRequestEvent(UUID eventId, Method method) {
        this.eventId = eventId;
        this.method = method;
    }

    public UUID getEventId() {
        return eventId;
    }

    public Method getMethod() {
        return method;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
