package com.richard.earthquake.app.data.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("last_earthquake")
public class LastEarthquakes extends Earthquake {
}
