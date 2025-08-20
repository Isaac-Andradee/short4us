package com.isaacandrade.resolverservice.unit_tests.resolver.application;

import com.isaacandrade.common.url.model.UrlMapping;
import com.isaacandrade.resolverservice.exception.KeyNotFoundException;
import com.isaacandrade.resolverservice.resolver.application.DbLookup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DbLookupTests {
    @Mock
    private DbLookup dbLookup;

    @Mock
    private UrlMapping urlMapping;

    @Test
    void shouldGetFromDb_when_key_exists() {
        when(dbLookup.getFromDb(urlMapping.getShortKey())).thenReturn(urlMapping);
        UrlMapping result = dbLookup.getFromDb(urlMapping.getShortKey());
        assertEquals(urlMapping, result);
    }

    @Test
    void shouldThrowException_when_key_was_not_found() {
        String key = "invalidKey";
        when(dbLookup.getFromDb(key)).thenThrow(new KeyNotFoundException("Key " + key + " Not Found"));
        assertThrows(KeyNotFoundException.class, () -> dbLookup.getFromDb(key));
    }

}
