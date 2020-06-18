package com.richard.earthquake.app.domain.service;

import com.richard.earthquake.app.data.model.ChatMessage;
import com.richard.earthquake.app.data.repo.ChatMessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;


@Service
public class ChatService {
    DirectProcessor<ChatMessage> messageReceiver;
    @Autowired
    ChatMessageRepo chatMessageRepo;

    ChatService(){
        messageReceiver = DirectProcessor.create();
    }

    @PostConstruct
    public void postConstruct(){
        insertAllSms();
    }

    private void insertAllSms() {
        chatMessageRepo.saveAll(getMessageStream()).subscribe();
    }

    public Flux<ChatMessage> getMessageStream() {
        return messageReceiver;
    }

    public ChatMessage addMessage(ChatMessage chatMessage) {
        System.out.println(chatMessage.toString());
        messageReceiver.onNext(chatMessage);
        return chatMessage;
    }


    public Flux<ChatMessage> getStreamForEarthquake(String earthquakeId) {
        return getMessageStream().filter(chatMessage -> chatMessage.getEarthquakeId().equals(earthquakeId));
    }
}
