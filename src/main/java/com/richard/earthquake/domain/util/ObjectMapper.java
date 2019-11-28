package com.richard.earthquake.domain.util;

import com.richard.earthquake.data.dto.EarthquakeDto;
import com.richard.earthquake.data.model.Earthquake;
import org.locationtech.jts.geom.*;

public class ObjectMapper {

    public static Earthquake map(EarthquakeDto earthquakeDto) {
        Earthquake earthquake = new Earthquake();
        earthquake.setType(earthquakeDto.getType());
        earthquake.setId(earthquakeDto.getId());
        earthquake.setProperties(earthquakeDto.getProperties());
        earthquake.setDepth(earthquakeDto.getGeometry().getCoordinates().get(2));
        earthquake.setGeometry(new GeometryFactory().createPoint(new Coordinate(earthquakeDto.getGeometry().getCoordinates().get(0), earthquakeDto.getGeometry().getCoordinates().get(1))));
        return earthquake;
    }
}
