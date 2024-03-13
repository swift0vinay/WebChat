package com.websocket.chat.config;

import com.websocket.chat.model.ChatMessage;
import com.websocket.chat.model.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
    
    private final SimpMessageSendingOperations messageTemplate;
    
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        // Event if user has left the chat
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String userName = (String) headerAccessor.getSessionAttributes().get("username");
        if (userName != null) {
            log.info("User disconnected: {}", userName);
            ChatMessage chatMessage = ChatMessage.builder()
                    .type(MessageType.LEFT)
                    .sender(userName)
                    .build();
            messageTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
    
}