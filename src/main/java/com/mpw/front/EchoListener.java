package com.mpw.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/echo")
public class EchoListener {

    private final SimpMessagingTemplate template;

    @Autowired
    public EchoListener(SimpMessagingTemplate template){
        this.template = template;
    }

    @RequestMapping("/{content}")
    public void onEchoCall(@PathVariable String content) {
        // sends the message to /topic/message
        this.template.convertAndSend("/topic/message", content);
    }

}