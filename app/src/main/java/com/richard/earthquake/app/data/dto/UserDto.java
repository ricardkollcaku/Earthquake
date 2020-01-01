package com.richard.earthquake.app.data.dto;

import com.richard.earthquake.app.data.model.Filter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @Id
    private String email;
    private String firstName;
    private String lastName;

}
