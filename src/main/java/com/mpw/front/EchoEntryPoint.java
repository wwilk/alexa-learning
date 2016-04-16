package com.mpw.front;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/api/echo")
public class EchoEntryPoint {
    private static final Logger log = LoggerFactory.getLogger(EchoEntryPoint.class);
    private final SimpMessagingTemplate template;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ConcurrentMap<UUID, DeferredResult<String>> results = new ConcurrentHashMap<>();

    private final long deferredResultTimeout;

    @Autowired
    public EchoEntryPoint(SimpMessagingTemplate template, @Value("${deferred-result.timeout}") long deferredResultTimeout){
        this.template = template;
        this.deferredResultTimeout = deferredResultTimeout;
    }

    /**
     * request from aws lambda proxy
     */
    @RequestMapping(method = RequestMethod.POST)
    public DeferredResult<String> onEchoCall(@RequestBody Method method){
        UUID messageId = UUID.randomUUID();

        sendRequestToFrontEnd(messageId, method);

        return deferResult(messageId);
    }

    /**
     * response from the front-end
     */
    @MessageMapping("/alexaResponse")
    public void sendResponseToAlexa(AlexaResponseEvent message){
        DeferredResult<String> result = results.get(message.getRequestId());
        result.setResult(message.getPayload());
    }

    /**
     * send request to front-end via web socket
     */
    private void sendRequestToFrontEnd(UUID messageId, Method method){
        AlexaRequestEvent event = new AlexaRequestEvent(messageId, method);
        this.template.convertAndSend("/topic/alexaRequest", messageToString(event));
    }

    /**
      * wait with the response to alexa, until there is a response from our front-end or timeout occurs
      */
    private DeferredResult<String> deferResult(UUID messageId){
        DeferredResult<String> result = new DeferredResult(deferredResultTimeout);
        result.onTimeout( () -> result.setResult("Timeout occurred. Maybe you should increase it?"));
        results.put(messageId, result);
        return result;
    }

    private String messageToString(AlexaRequestEvent event){
        try {
            return mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}