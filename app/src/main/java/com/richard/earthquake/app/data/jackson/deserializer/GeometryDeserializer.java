package com.richard.earthquake.app.data.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.geojson.GeoJsonReader;

import java.io.IOException;

public class GeometryDeserializer extends JsonDeserializer<Geometry> {
    @Override
    public Geometry deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {


        GeoJsonReader geoJsonReader = new GeoJsonReader();
        try {
            return geoJsonReader.read(jsonParser.getCodec().readTree(jsonParser).toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}