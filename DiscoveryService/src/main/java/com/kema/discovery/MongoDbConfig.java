package com.kema.discovery;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

@Configuration
public class MongoDbConfig extends AbstractMongoConfiguration{

    @Value( "${mongo.host:localhost}" )
    private String host;

    @Value( "${mongo.port:27017}")
    private int port;

    @Value( "${mongo.dbName}")
    private String dbname;

    @Bean
    public MongoClient mongoClient() {
        return new MongoClient(host, port);
    }

//    @Bean
//    public MongoTemplate mongoTemplate(){
//        return new MongoTemplate(mongoClient(),dbname);
//    }

    protected String getDatabaseName() {
        return dbname;
    }
}
