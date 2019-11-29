package com.richard.earthquake.app.data.jackson;

import com.mongodb.BasicDBObject;
import org.bson.Document;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.locationtech.jts.io.geojson.GeoJsonWriter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Converters {

    public static List<Converter> allConverters() {
        List<Converter> result = new ArrayList<>();

        result.add(new CreationDateReadingConverter());
        result.add(new UpdateDateReadingConverter());
        result.add(new GeoGsonWritingConverter());
        result.add(new GeoJsonReadingConverter());

        return result;
    }

    private static Document getDocument(BasicDBObject basicDBObject) {
        if (basicDBObject == null) return null;
        return new Document(basicDBObject.toMap());
    }

    @ReadingConverter
    public static class GeoJsonReadingConverter implements Converter<Document, Geometry> {

        @Override
        public Geometry convert(Document basicDBObject) {
            //     Document value = getDocument(basicDBObject);
            GeoJsonReader geoJsonReader = new GeoJsonReader();
            try {
                return geoJsonReader.read(basicDBObject.toJson());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @WritingConverter
    public static class GeoGsonWritingConverter implements Converter<Geometry, BasicDBObject> {

        @Override
        public BasicDBObject convert(Geometry geometry) {
            GeoJsonWriter geoJsonWriter = new GeoJsonWriter();
            return BasicDBObject.parse(geoJsonWriter.write(geometry));

        }
    }


    @ReadingConverter
    public static class CreationDateReadingConverter implements Converter<Date, Long> {

        @Nullable
        @Override
        public Long convert(Date date) {
            return date.getTime();
        }
    }

    @ReadingConverter
    public static class UpdateDateReadingConverter implements Converter<Date, Long> {

        @Nullable
        @Override
        public Long convert(Date date) {
            return date.getTime();
        }
    }

}
