package com.danilodps.application.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.kafka")
public record KafkaProperties(Bootstrap bootstrap) {
    public record Bootstrap(String servers) {}
}
