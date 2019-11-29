package com.richard.earthquake.app.data.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.geojson.GeoJsonWriter;

import java.io.IOException;

public class GeometrySerializer extends JsonSerializer<Geometry> {


    @Override
    public void serialize(Geometry geometry, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        GeoJsonWriter geoJsonWriter = new GeoJsonWriter();
        jsonGenerator.writeRawValue(geoJsonWriter.write(geometry));
    }
}
