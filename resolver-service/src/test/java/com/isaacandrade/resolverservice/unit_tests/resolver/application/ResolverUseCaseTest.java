package com.isaacandrade.resolverservice.unit_tests.resolver.application;

import com.isaacandrade.common.url.model.UrlMapping;
import com.isaacandrade.resolverservice.exception.KeyNotFoundException;
import com.isaacandrade.resolverservice.resolver.application.CacheLookup;
import com.isaacandrade.resolverservice.resolver.application.DbLookup;
import com.isaacandrade.resolverservice.resolver.application.ResolverUseCase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResolverUseCaseTest {

    @Mock
    private CacheLookup cacheLookup;

    @Mock
    private DbLookup dbLookup;

    @Mock
    private UrlMapping urlMapping;

    @InjectMocks
    private ResolverUseCase resolverUseCase;

    private static final String SHORT_KEY = "abc123";
    private static final String LONG_URL = "https://site.com";
    private static final UrlMapping staticUrlMapping = new UrlMapping(SHORT_KEY, LONG_URL, SHORT_KEY);

    @Test
    void shouldReturnUrlFromCache_whenCacheHit() {
        when(cacheLookup.get(SHORT_KEY)).thenReturn(staticUrlMapping);
        String result = resolverUseCase.resolve(SHORT_KEY);
        assertEquals(LONG_URL, result);
        verify(dbLookup, never()).getFromDb(anyString());
        verify(cacheLookup, never()).save(anyString(), any());
    }

    @Test
    void shouldReturnUrlFromDbAndCacheIt_whenCacheMiss() {
        when(cacheLookup.get(SHORT_KEY)).thenReturn(null);
        when(dbLookup.getFromDb(SHORT_KEY)).thenReturn(staticUrlMapping);
        String result = resolverUseCase.resolve(SHORT_KEY);
        assertEquals(LONG_URL, result);
        verify(dbLookup).getFromDb(SHORT_KEY);
        verify(cacheLookup).save(SHORT_KEY, staticUrlMapping);
    }

    @Test
    void shouldThrow_whenNotFoundAnywhere() {
        when(cacheLookup.get(SHORT_KEY)).thenReturn(null);
        when(dbLookup.getFromDb(SHORT_KEY)).thenThrow(new KeyNotFoundException());
        assertThrows(KeyNotFoundException.class, () -> resolverUseCase.resolve(SHORT_KEY));
        verify(cacheLookup).get(SHORT_KEY);
        verify(dbLookup).getFromDb(SHORT_KEY);
        verify(cacheLookup, never()).save(anyString(), any());
    }

    @Test
    @DisplayName("Should resolve URL from cache when urlMapping exists in cache")
    void resolve_WhenUrlurlMappingExistsInCache_ShouldReturnLongUrlFromCache() {

        when(urlMapping.getLongUrl()).thenReturn(LONG_URL);
        when(cacheLookup.get(SHORT_KEY)).thenReturn(urlMapping);

        String result = resolverUseCase.resolve(SHORT_KEY);

        assertEquals(LONG_URL, result);

        // Verify cache was checked
        verify(cacheLookup, times(1)).get(SHORT_KEY);

        // Verify database was not queried since cache hit occurred
        verify(dbLookup, never()).getFromDb(any());

        // Verify cache was not updated since urlMapping was already in cache
        verify(cacheLookup, never()).save(any(), any());
    }

    @Test
    @DisplayName("Should resolve URL from database when urlMapping does not exist in cache")
    void resolve_WhenUrlMappingNotInCache_ShouldReturnLongUrlFromDatabaseAndSaveToCache() {

        when(urlMapping.getLongUrl()).thenReturn(LONG_URL);
        when(cacheLookup.get(SHORT_KEY)).thenReturn(null);
        when(dbLookup.getFromDb(SHORT_KEY)).thenReturn(urlMapping);

        String result = resolverUseCase.resolve(SHORT_KEY);

        assertEquals(LONG_URL, result);

        // Verify cache was checked first
        verify(cacheLookup, times(1)).get(SHORT_KEY);

        // Verify database was queried after cache miss
        verify(dbLookup, times(1)).getFromDb(SHORT_KEY);

        // Verify the retrieved urlMapping was saved to cache
        verify(cacheLookup, times(1)).save(SHORT_KEY, urlMapping);
    }

    @Test
    @DisplayName("Should verify correct interaction order when cache miss occurs")
    void resolve_WhenCacheMiss_ShouldFollowCorrectExecutionOrder() {

        when(urlMapping.getLongUrl()).thenReturn(LONG_URL);
        when(cacheLookup.get(SHORT_KEY)).thenReturn(null);
        when(dbLookup.getFromDb(SHORT_KEY)).thenReturn(urlMapping);

        resolverUseCase.resolve(SHORT_KEY);

        // Then - verify the order of operations
        var inOrder = inOrder(cacheLookup, dbLookup);
        inOrder.verify(cacheLookup).get(SHORT_KEY);
        inOrder.verify(dbLookup).getFromDb(SHORT_KEY);
        inOrder.verify(cacheLookup).save(SHORT_KEY, urlMapping);
    }

    @Test
    @DisplayName("Should handle different short keys correctly")
    void resolve_WithDifferentShortKeys_ShouldPassCorrectKeyToAllMethods() {

        String customShortKey = "xyz789";
        String customLongUrl = "https://www.different-example.com";
        UrlMapping customUrlMapping = mock(UrlMapping.class);

        when(customUrlMapping.getLongUrl()).thenReturn(customLongUrl);
        when(cacheLookup.get(customShortKey)).thenReturn(null);
        when(dbLookup.getFromDb(customShortKey)).thenReturn(customUrlMapping);

        String result = resolverUseCase.resolve(customShortKey);

        assertEquals(customLongUrl, result);
        verify(cacheLookup).get(customShortKey);
        verify(dbLookup).getFromDb(customShortKey);
        verify(cacheLookup).save(customShortKey, customUrlMapping);
    }

    @Test
    @DisplayName("Should verify UrlMapping.getLongUrl() is called exactly once")
    void resolve_ShouldCallGetLongUrlOnlyOnce() {

        when(urlMapping.getLongUrl()).thenReturn(LONG_URL);
        when(cacheLookup.get(SHORT_KEY)).thenReturn(urlMapping);

        resolverUseCase.resolve(SHORT_KEY);

        verify(urlMapping, times(1)).getLongUrl();
    }
}
