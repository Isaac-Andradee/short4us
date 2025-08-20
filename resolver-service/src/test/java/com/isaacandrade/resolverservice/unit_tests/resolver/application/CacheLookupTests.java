package com.isaacandrade.resolverservice.unit_tests.resolver.application;

import com.isaacandrade.common.url.model.UrlMapping;
import com.isaacandrade.resolverservice.resolver.application.CacheLookup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CacheLookupTests {
    @Mock
    private CacheLookup cacheLookup;

    @Mock
    private UrlMapping urlMapping;

    @Test
    void shouldGetUrlFromCache_when_key_found() {
        String shortKey = "xdSc2ts";
        when(cacheLookup.get(shortKey)).thenReturn(urlMapping);
        UrlMapping result = cacheLookup.get(shortKey);
        assert result.equals(urlMapping);
    }

    @Test
    void shouldReturnNull_when_key_not_found() {
        String shortKey = "xdSc2ts";
        when(cacheLookup.get(shortKey)).thenReturn(null);
        UrlMapping result = cacheLookup.get(shortKey);
        assert result == null;
    }

    @Test
    void shouldSaveUrlToCache_when_key_not_found_in_db() {
        String shortKey = "xdSc2ts";
        when(cacheLookup.get(shortKey)).thenReturn(urlMapping);
        cacheLookup.save(shortKey, urlMapping);
        assertEquals(urlMapping, cacheLookup.get(shortKey));
    }

    @Test
    void shouldReturnNull_when_savingNullUrl() {
        String shortKey = "xdSc2ts";
        cacheLookup.save(shortKey, null);
        UrlMapping result = cacheLookup.get(shortKey);
        assertEquals(null, result); 
    }
}