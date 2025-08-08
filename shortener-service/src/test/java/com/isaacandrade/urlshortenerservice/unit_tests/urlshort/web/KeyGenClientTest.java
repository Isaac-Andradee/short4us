package com.isaacandrade.urlshortenerservice.unit_tests.urlshort.web;

import com.isaacandrade.urlshortenerservice.urlshort.exception.KeygenServiceUnvailableException;
import com.isaacandrade.urlshortenerservice.web.keygen.KeyGenClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SuppressWarnings("rawtypes")
public class KeyGenClientTest {

    @Mock private WebClient.Builder mockBuilder;
    @Mock private WebClient mockWebClient;

    // Use raw types to avoid generics conflict
    @Mock private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock private WebClient.ResponseSpec responseSpec;

    private KeyGenClient keyGenClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockBuilder.build()).thenReturn(mockWebClient);
        keyGenClient = new KeyGenClient(mockBuilder);
    }

    @Test
    void testGenerateKey_Success() {
        // Arrange
        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("shortKey", "abc123");

        when(mockWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://keygen-service/generate")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(responseMap));

        // Act
        String key = keyGenClient.generateKey();

        // Assert
        assertEquals("abc123", key);
    }

    @Test
    void testGenerateKey_ServiceUnavailable() {
        // Arrange
        when(mockWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://keygen-service/generate")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenThrow(new RuntimeException("Service error"));

        // Act & Assert
        KeygenServiceUnvailableException ex = assertThrows(
                KeygenServiceUnvailableException.class,
                () -> keyGenClient.generateKey()
        );

        assertEquals("Keygen Service Is Unavailable", ex.getMessage());
    }
}
