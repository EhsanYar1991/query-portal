package com.yar.iot.queryportal.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * The test configurations
 * */
@TestConfiguration
public class TestConfig {

    /**
     * Custom the MongoDB auditing event for test purposes
     * */
    @Bean
    public CustomAuditingEventListener customAuditConfig() {
        return new CustomAuditingEventListener();
    }

}
