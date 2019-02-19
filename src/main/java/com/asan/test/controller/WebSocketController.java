package com.asan.test.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/sendMessage")
    public void processMessage(@Payload String message, Principal principal) throws Exception {

        logger.info("processMessage method");
        logger.info("user message: " + message);
        messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/reply", principal.getName() + " says " + message);

    }

    @MessageExceptionHandler
    public void handleException(Throwable exception, Principal principal) {
        logger.info("handleException method");
        messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/errors", exception.getMessage());
    }
}
