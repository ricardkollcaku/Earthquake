package com.richard.earthquake.app.data.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.richard.earthquake.app.data.jackson.deserializer.GeometryDeserializer;
import com.richard.earthquake.app.data.jackson.serializer.GeometrySerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;

@Data
@NoArgsConstructor
public class Filter {

    String name;
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Geometry geometry;
    private double minMagnitude;

}
