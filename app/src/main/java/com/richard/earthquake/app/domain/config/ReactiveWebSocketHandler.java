package com.richard.earthquake.app.domain.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.richard.earthquake.app.data.model.ChatMessage;
import com.richard.earthquake.app.domain.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component("ReactiveWebSocketHandler")
public class ReactiveWebSocketHandler implements WebSocketHandler {
    @Autowired
    ChatService chatService;
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        if (webSocketSession.getHandshakeInfo().getHeaders().get("earthquakeId") == null || webSocketSession.getHandshakeInfo().getHeaders().get("earthquakeId").get(0) == null)
            return Mono.empty();
        String earthquakeId = webSocketSession.getHandshakeInfo().getHeaders().get("earthquakeId").get(0);

        return webSocketSession.send(
                chatService.getStreamForEarthquake(earthquakeId)
                        .flatMap(this::getJsonFromChatMessage)
                        .map(webSocketSession::textMessage).log())
                .and(webSocketSession.receive()
                        .map(WebSocketMessage::getPayloadAsText)
                        .flatMap(this::getChatMessageFromJson)
                        .map(chatService::addMessage))
                ;
    }

    Mono<String> getJsonFromChatMessage(ChatMessage chatMessage) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"message\":\"");
        stringBuilder.append(chatMessage.getMessage());
        stringBuilder.append("\",\"earthquakeId\":\"");
        stringBuilder.append(chatMessage.getEarthquakeId());
        stringBuilder.append("\",\"id\":\"");
        stringBuilder.append(chatMessage.getId());
        stringBuilder.append("\",\"createdTime\":");
        stringBuilder.append(chatMessage.getCreatedTime());
        stringBuilder.append(",\"user\":{\"id\":\"");
        stringBuilder.append(chatMessage.getUser().getId());
        stringBuilder.append("\",\"name\":\"");
        stringBuilder.append(chatMessage.getUser().getName());
        stringBuilder.append("\",\"lastName\":\"");
        stringBuilder.append(chatMessage.getUser().getLastName());
        stringBuilder.append("\"}}");
        return Mono.just(stringBuilder.toString());
    }


    Mono<ChatMessage> getChatMessageFromJson(String json) {
        try {
            ChatMessage chatMessage = objectMapper.readValue(json, ChatMessage.class);
            chatMessage.setCreatedTime(System.currentTimeMillis());
            return Mono.just(chatMessage);
        } catch (JsonProcessingException e) {
            System.out.println(e);
            return Mono.empty();
        }
    }

}
