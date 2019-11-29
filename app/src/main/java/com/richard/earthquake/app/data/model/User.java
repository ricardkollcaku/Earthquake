package com.richard.earthquake.app.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@NoArgsConstructor
public class User {

    @Id
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private List<Filter> filters;
    private String token;


}
