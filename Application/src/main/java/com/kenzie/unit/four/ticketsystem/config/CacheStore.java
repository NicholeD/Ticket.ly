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
        // Write your code here
        // Retrieve and return the concert
        return null;
    }

    public void evict(String key) {
        // Write your code here
        // Invalidate/evict the concert from cache
    }

    public void add(String key, Concert value) {
        // Write your code here
        // Add concert to cache
    }
}
