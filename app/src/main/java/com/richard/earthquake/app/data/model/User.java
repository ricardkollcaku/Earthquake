package com.richard.earthquake.app.data.model;

import com.sun.el.parser.Token;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;
@Document("user")

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private List<Filter> filters;
    private Set<String> tokens;


    public String getSystemRole() {
        return "User";
    }
}
