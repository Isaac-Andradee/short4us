package com.isaacandrade.urlshortenerservice.unit_tests.urlshort.config;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import com.isaacandrade.urlshortenerservice.config.CacheConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class CacheConfigTest {
    
    @Test
    void cacheManager_shouldReturnConfiguredRedisCacheManager() {
        //Arrange
        RedisConnectionFactory mockConnectionFactory = mock(RedisConnectionFactory.class);
        CacheConfig cacheConfig = new CacheConfig();

        //act
        RedisCacheManager cacheManager = cacheConfig.cacheManager(mockConnectionFactory);

        //assert
        assertNotNull(cacheManager, "The RedisCacheManager should not be null");
    }
}
