package com.richard.earthquake.processor.data.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.richard.earthquake.processor.data.jackson.deserializer.GeometryDeserializer;
import com.richard.earthquake.processor.data.jackson.serializer.GeometrySerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("earthquake")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Earthquake {
    private String country;
    private String countryCode;
    private String type;
    private Properties properties;
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Geometry geometry;
    @Id
    private String id;
    private double depth;

}
