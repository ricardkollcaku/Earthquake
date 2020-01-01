package com.richard.earthquake.processor.data.dto;

import com.richard.earthquake.processor.data.model.Properties;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private long time;

    @Data
    @NoArgsConstructor
    public class Geometry {
        List<Double> coordinates;
    }
}
