package com.richard.earthquake.data.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GeoJsonDeserializer extends JsonDeserializer<GeoJson> {
    @Override
    public GeoJson deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String type = node.get("type").textValue();
        switch (type) {
            case "Point":
                return new GeoJsonPoint(getPoint(node.get("coordinates")));
            case "LineString":
                return new GeoJsonLineString(getPoints(node.get("coordinates")));
            case "MultiPoint":
                return new GeoJsonMultiPoint(getPoints(node.get("coordinates")));
            case "Polygon":
                return getPolygon(node.get("coordinates"));
            case "MultiPolygon":
                return getMultiPolygon(node.get("coordinates"));
        }
        return null;
    }

    private GeoJsonPolygon getPolygon(JsonNode coordinates) {
        GeoJsonPolygon result;
        Iterator<JsonNode> iterator = coordinates.iterator();
        result = iterator.hasNext() ? new GeoJsonPolygon(getPoints(iterator.next())) : null;
        iterator.forEachRemaining(jsonNode -> {
            if (result != null) {
                result.withInnerRing(getPoints(jsonNode));
            }
        });
        return result;
    }

    private GeoJsonMultiPolygon getMultiPolygon(JsonNode coordinates) {
        List<GeoJsonPolygon> polygons = new ArrayList<>();
        coordinates.forEach(jsonNode -> polygons.add(getPolygon(jsonNode)));
        return new GeoJsonMultiPolygon(polygons);

    }

    private Point getPoint(JsonNode jsonNode) {
        double latitude = jsonNode.get(0).asDouble();
        double longitude = jsonNode.get(1).asDouble();
        return new Point(latitude, longitude);
    }

    private List<Point> getPoints(JsonNode jsonNode) {
        List<Point> result = new ArrayList<>();
        jsonNode.forEach((elem) -> result.add(getPoint(elem)));
        return result;
    }

}