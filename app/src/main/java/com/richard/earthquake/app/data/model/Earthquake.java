package com.richard.earthquake.app.data.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.richard.earthquake.app.data.jackson.deserializer.GeoJsonDeserializer;
import com.richard.earthquake.app.data.jackson.serializer.GeoJsonSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJson;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("earthquake")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Earthquake {
    private String type;
    private String countryCode;
    private String country;
    private Short countryKey;
    private Properties properties;
    private long modifiedTime;
    private long time;
    private double mag;
    @JsonSerialize(using = GeoJsonSerializer.class)
    @JsonDeserialize(using = GeoJsonDeserializer.class)
    private GeoJson geometry;
    @Id
    private String id;
    private double depth;

}