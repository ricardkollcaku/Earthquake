package com.richard.earthquake.app.data.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.richard.earthquake.app.data.jackson.deserializer.GeoJsonDeserializer;
import com.richard.earthquake.app.data.jackson.deserializer.GeometryDeserializer;
import com.richard.earthquake.app.data.jackson.serializer.GeoJsonSerializer;
import com.richard.earthquake.app.data.jackson.serializer.GeometrySerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.mongodb.core.geo.GeoJson;

@Data
@NoArgsConstructor
public class Filter {

    String name;
    @JsonSerialize(using = GeoJsonSerializer.class)
    @JsonDeserialize(using = GeoJsonDeserializer.class)
    private GeoJson geometry;
    private double minMagnitude;

}
