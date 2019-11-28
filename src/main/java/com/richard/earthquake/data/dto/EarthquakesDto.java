package com.richard.earthquake.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.richard.earthquake.data.model.Earthquake;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Data
public class EarthquakesDto {

    @JsonProperty("features")
    List<EarthquakeDto> earthquakes;
}
