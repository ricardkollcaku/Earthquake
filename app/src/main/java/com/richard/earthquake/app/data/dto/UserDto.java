package com.richard.earthquake.app.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @Id
    private String email;
    private String firstName;
    private String lastName;
    private Boolean isNotificationEnabled;


}
