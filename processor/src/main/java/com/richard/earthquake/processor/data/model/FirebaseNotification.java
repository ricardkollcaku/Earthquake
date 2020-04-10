package com.richard.earthquake.processor.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirebaseNotification {
    private Object data;
    private String to;

}
