package com.richard.earthquake.processor.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class EarthquakesDto {

    @JsonProperty("features")
    List<EarthquakeDto> earthquakes;
}
