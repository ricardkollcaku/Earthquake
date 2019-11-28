package com.richard.earthquake.domain;


import com.mongodb.MongoClient;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.reactivestreams.client.gridfs.GridFSBucket;
import com.mongodb.reactivestreams.client.gridfs.GridFSBuckets;
import com.richard.earthquake.data.jackson.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableMongoAuditing
public class MongoConfig {

    private final MongoDbFactory mongoDbFactory;
    private final MongoMappingContext mongoMappingContext;
    @Autowired
    MongoClient mongoClient;
    @Value("${spring.data.mongodb.database}")
    String name;

    @Autowired
    public MongoConfig(MongoDbFactory mongoDbFactory, MongoMappingContext mongoMappingContext) {
        this.mongoDbFactory = mongoDbFactory;
        this.mongoMappingContext = mongoMappingContext;
    }


    @Bean
    public GridFSBucket gridFsBucket(ReactiveMongoTemplate reactiveMongoClient) throws Exception {
        return GridFSBuckets.create(reactiveMongoClient.getMongoDatabase());
    }

    @Bean
    public MappingMongoConverter mappingMongoConverter() {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
        MappingMongoConverter mongoMapping = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        mongoMapping.setCustomConversions(customConversions()); // tell mongodb to use the custom converters
        mongoMapping.setTypeMapper(new DefaultMongoTypeMapper(null));
        mongoMapping.afterPropertiesSet();
        return mongoMapping;
    }

    private CustomConversions customConversions() {
        List<Converter> converters = new ArrayList<>(Converters.allConverters());
        return new MongoCustomConversions(converters);
    }

}
