package com.richard.earthquake.processor.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("countries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    @Id
    String country;
    String countryCode;

    public Country(String country) {
        this.country = country;
    }
}
