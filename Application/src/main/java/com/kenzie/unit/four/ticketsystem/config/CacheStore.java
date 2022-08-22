package com.kenzie.unit.four.ticketsystem.config;

import com.kenzie.unit.four.ticketsystem.service.model.Concert;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

public class CacheStore {
    private Cache<String, Concert> cache;

    public CacheStore(int expiry, TimeUnit timeUnit) {
        // initalize the cache
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiry, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();    
    }

    public Concert get(String key) {
        return cache.getIfPresent(key);
    }

    public void evict(String key) {
        cache.invalidate(key);
    }

    public void add(String key, Concert value) {
        cache.put(key, value);
    }
}
