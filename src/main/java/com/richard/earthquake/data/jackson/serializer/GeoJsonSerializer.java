package com.richard.earthquake.data.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.*;

import java.io.IOException;

public class GeoJsonSerializer extends JsonSerializer<GeoJson> {
    @Override
    public void serialize(GeoJson value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        switch (value.getType()) {
            case "Point":
                gen.writeStartObject();
                gen.writeStringField("type", value.getType());
                gen.writeObjectField("coordinates", value.getCoordinates());
                gen.writeEndObject();
                break;
            case "LineString":
                gen.writeStartObject();
                gen.writeStringField("type", value.getType());
                gen.writeArrayFieldStart("coordinates");
                GeoJsonLineString geoJsonLineString = (GeoJsonLineString) value;
                for (Point p : geoJsonLineString.getCoordinates()) {
                    gen.writeObject(new double[]{p.getX(), p.getY()});
                }
                gen.writeEndArray();
                gen.writeEndObject();
                break;
            case "MultiPoint":
                gen.writeStartObject();
                gen.writeStringField("type", value.getType());
                gen.writeArrayFieldStart("coordinates");
                GeoJsonMultiPoint geoJsonMultiPoint = (GeoJsonMultiPoint) value;
                for (Point p : geoJsonMultiPoint.getCoordinates()) {
                    gen.writeObject(new double[]{p.getX(), p.getY()});
                }
                gen.writeEndArray();
                gen.writeEndObject();
                break;
            case "Polygon":
                gen.writeStartObject();
                gen.writeStringField("type", value.getType());
                gen.writeArrayFieldStart("coordinates");
                GeoJsonPolygon geoJsonPolygon = (GeoJsonPolygon) value;
                gen.writeStartArray(geoJsonPolygon.getCoordinates().size());
                for (GeoJsonLineString elem : geoJsonPolygon.getCoordinates()) {
                    for (Point p : elem.getCoordinates()) {
                        gen.writeObject(new double[]{p.getX(), p.getY()});
                    }
                }
                gen.writeEndArray();
                gen.writeEndArray();
                gen.writeEndObject();
                break;

            case "MultiPolygon":

                gen.writeStartObject();
                gen.writeStringField("type", "MultiPolygon");
                gen.writeArrayFieldStart("coordinates");
                GeoJsonMultiPolygon geoJsonMultiPolygon = (GeoJsonMultiPolygon) value;
                for (int i = 0; i != geoJsonMultiPolygon.getCoordinates().size(); ++i) {

                    gen.writeStartArray();
                    GeoJsonPolygon geoJsonPolygon1 = geoJsonMultiPolygon.getCoordinates().get(i);
                    gen.writeStartArray(geoJsonPolygon1.getCoordinates().size());
                    for (GeoJsonLineString elem : geoJsonPolygon1.getCoordinates()) {
                        for (Point p : elem.getCoordinates()) {
                            gen.writeObject(new double[]{p.getX(), p.getY()});
                        }
                    }
                    gen.writeEndArray();
                    gen.writeEndArray();

                }

                gen.writeEndArray();
                gen.writeEndObject();

                break;

        }

    }

}