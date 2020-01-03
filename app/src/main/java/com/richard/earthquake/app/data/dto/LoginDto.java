package com.richard.earthquake.app.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginDto {
    String email;
    String password;

    public String getEmail() {
        return email != null ? email : "";
    }

    public String getPassword() {
        return password != null ? password : "";
    }
}
