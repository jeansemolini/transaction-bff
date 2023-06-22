package br.com.coffeeandit.transactionbff.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestRedisConfiguration {

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Bean
    public RedisProperties redisProperties() {
        var redisProperties = new RedisProperties();
        redisProperties.setHost(redisHost);
        redisProperties.setPort(redisPort);
        return redisProperties;
    }
}
