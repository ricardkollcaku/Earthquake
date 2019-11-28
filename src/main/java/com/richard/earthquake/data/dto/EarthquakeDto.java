package com.richard.earthquake.data.dto;

import com.richard.earthquake.data.model.Properties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@NoArgsConstructor
public class EarthquakeDto {
    private String type;
    private Properties properties;
    private Geometry geometry;
    @Id
    private String id;

    @Data
    @NoArgsConstructor
    public class Geometry {
        List<Double> coordinates;
    }
}
