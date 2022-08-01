package com.kenzie.unit.four.ticketsystem.service;

import com.kenzie.unit.four.ticketsystem.IntegrationTest;
import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class MetricsRegistryTest {
    @Autowired
    CacheManager cacheManager;

    @Autowired
    ConcertService concertService;

    @Autowired
    private MeterRegistry registry;

//    @Test
//    public void concertCache_TestMetricCacheHitExists() throws Exception {
//        FunctionCounter counter = registry
//                .get("cache.gets")
//                .tags("cache", "concert_cache", "cacheManager", "cacheManager", "name", "concert_cache", "result", "hit")
//                .functionCounter();
//
//        assertThat(counter).isNotNull();
//    }
//
//    @Test
//    public void concertCache_TestMetricCountMissExists() throws Exception {
//        FunctionCounter counter = registry
//                .get("cache.gets")
//                .tags("cache", "concert_cache", "cacheManager", "cacheManager", "name", "concert_cache", "result", "miss")
//                .functionCounter();
//
//        assertThat(counter).isNotNull();
//    }
}
