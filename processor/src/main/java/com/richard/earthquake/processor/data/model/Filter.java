package com.richard.earthquake.processor.data.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.richard.earthquake.processor.data.jackson.deserializer.GeometryDeserializer;
import com.richard.earthquake.processor.data.jackson.serializer.GeometrySerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filter {
    String name;
    String country;
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Geometry geometry;
    private double minMagnitude;


}
