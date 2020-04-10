package com.richard.earthquake.processor.data.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("last_earthquake")
public class LastEarthquakes extends Earthquake {
}
