package com.votesecure.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * Disables Redis auto-configuration for development.
 * We use in-memory DistributedLockService instead.
 */
@Configuration
@EnableAutoConfiguration(exclude = {RedisAutoConfiguration.class,
        org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration.class})
public class RedisConfig {
}
