package com.richard.earthquake.app.data.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("chat_message")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChatMessage {
    @Id
    String id;
    ChatUser user;
    String message;
    String earthquakeId;
    Long createdTime;
}
