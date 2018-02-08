package com.weather.cache;

import com.google.api.client.json.GenericJson;
import lombok.Getter;
import lombok.Setter;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;


public class CacheHelper {

    @Getter
    @Setter
    private CacheManager manager;

    @Getter
    @Setter
    private Cache<String, GenericJson> responseCache;

    public CacheHelper() {
        manager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("responseCache",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, GenericJson.class,
                                ResourcePoolsBuilder.heap(10))
                                .build())
                .build(true);

        responseCache = manager.getCache("responseCache", String.class, GenericJson.class);
    }

    public Cache<String, GenericJson> getResponseCache() {
        return manager.getCache("responseCache", String.class, GenericJson.class);
    }
}
