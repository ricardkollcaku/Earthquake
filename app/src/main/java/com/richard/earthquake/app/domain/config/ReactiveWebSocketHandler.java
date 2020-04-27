package com.richard.earthquake.app.domain.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.async.ByteArrayFeeder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.richard.earthquake.app.data.model.ChatMessage;
import com.richard.earthquake.app.data.model.ChatUser;
import com.richard.earthquake.app.domain.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

@Component("ReactiveWebSocketHandler")
public class ReactiveWebSocketHandler implements WebSocketHandler {
    @Autowired
    ChatService chatService;
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {

        String earthquakeId = "ci39380544";

        return webSocketSession.send(
                chatService.getStreamForEarthquake(earthquakeId)
                .flatMap(this::getJsonFromChatMessage)
                .map(webSocketSession::textMessage).log())
                .and(webSocketSession.receive()
                .map(WebSocketMessage::getPayloadAsText).log())
              ;
    }

    Mono<String> getJsonFromChatMessage(ChatMessage chatMessage){
    StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"message\":\"");
        stringBuilder.append(chatMessage.getMessage());
        stringBuilder.append("\",\"earthquakeId\":\"");
        stringBuilder.append(chatMessage.getEarthquakeId());
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


    void test(){
        Jackson2JsonDecoder jackson2JsonDecoder = new Jackson2JsonDecoder();

    }

}
