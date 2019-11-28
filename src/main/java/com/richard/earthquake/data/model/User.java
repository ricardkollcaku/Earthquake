package com.richard.earthquake.data.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.richard.earthquake.data.jackson.deserializer.GeometryDeserializer;
import com.richard.earthquake.data.jackson.serializer.GeometrySerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.awt.PointShapeFactory;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

@Data
@NoArgsConstructor
public class User {

    @Id
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Geometry coordinates;
    private String token;


}
