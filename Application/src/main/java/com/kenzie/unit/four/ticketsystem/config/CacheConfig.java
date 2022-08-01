package com.kenzie.unit.four.ticketsystem.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheStore concertCache() {
        return new CacheStore(120, TimeUnit.SECONDS);
    }
}
