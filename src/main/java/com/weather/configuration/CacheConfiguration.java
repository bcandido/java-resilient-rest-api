package com.weather.configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfiguration {

    public static final String SHORT_LIVED_CITY_WEATHER = "weather";

    @Value("${com.weather.cache.initialCapacity}")
    private int initialCapacity;

    @Value("${com.weather.cache.maximumSize}")
    private int maximumSize;

    @Value("${com.weather.cache.expireTtlSeconds}")
    private long expireTtlSeconds;

    @Bean(SHORT_LIVED_CITY_WEATHER)
    public CaffeineCache weatherCache() {
        Cache<Object, Object> cache = Caffeine.newBuilder()
                .initialCapacity(initialCapacity)
                .maximumSize(maximumSize)
                .expireAfterWrite(expireTtlSeconds, TimeUnit.SECONDS)
                .build();
        return new CaffeineCache(SHORT_LIVED_CITY_WEATHER, cache);
    }
}
