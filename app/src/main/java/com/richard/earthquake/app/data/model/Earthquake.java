package com.richard.earthquake.app.data.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.richard.earthquake.app.data.jackson.deserializer.CustomDateTimeDeserializer;
import com.richard.earthquake.app.data.jackson.deserializer.GeometryDeserializer;
import com.richard.earthquake.app.data.jackson.serializer.CustomDateTimeSerializer;
import com.richard.earthquake.app.data.jackson.serializer.GeometrySerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.locationtech.jts.geom.Point;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("earthquake")
@Data
@NoArgsConstructor

public class Earthquake {
    private String type;
    private Properties properties;
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(using = GeometryDeserializer.class)
    private Point geometry;
    @Id
    private String id;
    private double depth;


}
