package com.yar.iot.queryportal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * MongoDB Configuration
 * */
@Configuration
@EnableMongoAuditing
public class MongoConfig {

    /**
     * Create {@link MongoTransactionManager} to managing database transactions
     * @param dbFactory {@link MongoDatabaseFactory}
     * @return {@link MongoTransactionManager}
     * */
    @Bean
    public MongoTransactionManager transactionManager(final MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

}
