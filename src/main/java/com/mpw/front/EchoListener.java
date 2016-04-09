package com.mpw.front;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class EchoListener {
    private static final Logger log = LoggerFactory.getLogger(EchoListener.class);
    private final SimpMessagingTemplate template;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ConcurrentMap<UUID, DeferredResult<String>> results = new ConcurrentHashMap<>();

    @Autowired
    public EchoListener(SimpMessagingTemplate template){
        this.template = template;
    }

    @RequestMapping("/{content}")
    public DeferredResult<String> onEchoCall(@PathVariable String content) {
        UUID messageId = UUID.randomUUID();

        Method method = fromContent(content);
        AlexaRequestEvent event = new AlexaRequestEvent(messageId, method);
        this.template.convertAndSend("/topic/alexaRequest", messageToString(event));
        DeferredResult result = new DeferredResult(100000l);
        result.onTimeout( () -> result.setResult("timeout"));
        results.put(messageId, result);
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    public DeferredResult<String> onEchoCall(@RequestBody Method method){
        UUID messageId = UUID.randomUUID();
        AlexaRequestEvent event = new AlexaRequestEvent(messageId, method);
        this.template.convertAndSend("/topic/alexaRequest", messageToString(event));
        DeferredResult result = new DeferredResult(100000l);
        result.onTimeout( () -> result.setResult("timeout"));
        results.put(messageId, result);
        return result;
    }

    @MessageMapping("/alexaResponse")
    public void sendResponseToAlexa(AlexaResponseEvent message){
        DeferredResult<String> result = results.get(message.getRequestId());
        result.setResult(message.getPayload());
    }

    private String messageToString(AlexaRequestEvent event){
        try {
            return mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Method fromContent(String content){
        if(!content.contains("-")){
            return new Method(content, Collections.emptyMap());
        }
        String[] contentParts = content.split("-");
        Map<String, String> parameters = new HashMap<>();
        for(int i=1;i<contentParts.length-1;i++){
            parameters.put(contentParts[i], contentParts[i+1]);
        }
        return new Method(contentParts[0], parameters);
    }

}