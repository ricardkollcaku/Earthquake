package com.richard.earthquake.processor.data.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sun.el.parser.Token;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.awt.PointShapeFactory;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class User {

    @Id
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private List<Filter> filters;
    private Set<String> tokens;

}
