package com.hsx.processor.db;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.hsx.processor.repository")
public class MongoDBConfig {

    @Value("${spring.data.mongodb.uri}")
    private String connectionString;

    @Bean
    public MongoDbFactory mongoDbFactory() {
        MongoDbFactory factory = new SimpleMongoClientDbFactory(connectionString);
        return factory;
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        MongoTemplate template = new MongoTemplate(mongoDbFactory());
        return template;
    }
} 