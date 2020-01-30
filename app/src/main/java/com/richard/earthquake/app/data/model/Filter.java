package com.richard.earthquake.app.data.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.richard.earthquake.app.data.jackson.deserializer.GeoJsonDeserializer;
import com.richard.earthquake.app.data.jackson.serializer.GeoJsonSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filter {

    String name;
    String country;
    @JsonSerialize(using = GeoJsonSerializer.class)
    @JsonDeserialize(using = GeoJsonDeserializer.class)
    private GeoJsonPolygon geometry;
    private double minMagnitude;


}
