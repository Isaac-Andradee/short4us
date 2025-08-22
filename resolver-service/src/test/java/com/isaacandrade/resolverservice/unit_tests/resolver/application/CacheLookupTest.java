package com.isaacandrade.resolverservice.unit_tests.resolver.application;

import com.isaacandrade.common.url.model.UrlMapping;
import com.isaacandrade.resolverservice.resolver.application.CacheLookup;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.when;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static com.isaacandrade.resolverservice.resolver.utils.ResolverConstants.CACHE_NAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CacheLookupTest {

    private CacheManager cacheManager;
    private Cache cache;
    private CacheLookup cacheLookup;

    @BeforeEach
    void setUp() {
        cacheManager = mock(CacheManager.class);
        cache = mock(Cache.class);
        when(cacheManager.getCache(CACHE_NAME)).thenReturn(cache);
        cacheLookup = new CacheLookup(cacheManager);
    }

    @Test
    void testGetWhenValueExists() {
        String shortKey = "abc123";
        UrlMapping expectedMapping = new UrlMapping();
        Cache.ValueWrapper valueWrapper = mock(Cache.ValueWrapper.class);

        when(cache.get(shortKey)).thenReturn(valueWrapper);
        when(valueWrapper.get()).thenReturn(expectedMapping);

        UrlMapping actual = cacheLookup.get(shortKey);

        assertNotNull(actual);
        assertEquals(expectedMapping, actual);
        verify(cacheManager).getCache(CACHE_NAME);
        verify(cache).get(shortKey);
    }

    @Test
    void testGetWhenValueDoesNotExist() {
        String shortKey = "notfound";

        when(cache.get(shortKey)).thenReturn(null);

        UrlMapping result = cacheLookup.get(shortKey);

        assertNull(result);
        verify(cacheManager).getCache(CACHE_NAME);
        verify(cache).get(shortKey);
    }

    @Test
    void testSave() {
        String shortKey = "xyz789";
        UrlMapping urlMapping = new UrlMapping();

        cacheLookup.save(shortKey, urlMapping);

        verify(cacheManager).getCache(CACHE_NAME);
        verify(cache).put(shortKey, urlMapping);
    }
}