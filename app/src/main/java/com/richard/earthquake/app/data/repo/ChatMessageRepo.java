package com.richard.earthquake.app.data.repo;

import com.richard.earthquake.app.data.model.ChatMessage;
import com.richard.earthquake.app.data.model.Country;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ChatMessageRepo extends ReactiveMongoRepository<ChatMessage, String> {

}
