package com.richard.earthquake.app.data.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("chat_message")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatMessage {
    ChatUser user;
    String message;
    String earthquakeId;
    Long createdTime;
}
