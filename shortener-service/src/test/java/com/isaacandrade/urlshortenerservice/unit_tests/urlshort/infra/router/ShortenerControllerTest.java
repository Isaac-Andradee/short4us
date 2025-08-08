package com.isaacandrade.urlshortenerservice.unit_tests.urlshort.infra.router;

import com.isaacandrade.urlshortenerservice.urlshort.application.ShortenerUseCase;
import com.isaacandrade.common.url.model.dto.ShortenRequest;
import com.isaacandrade.common.url.model.dto.ShortenResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.isaacandrade.urlshortenerservice.urlshort.infra.router.ShortenerController;

class ShortenerControllerTest {

    private ShortenerUseCase shortenerUseCase;
    private ShortenerController shortenerController;

    @BeforeEach
    void setUp() {
        shortenerUseCase = mock(ShortenerUseCase.class);
        shortenerController = new ShortenerController(shortenerUseCase);
    }

    @Test
    void testShortenUrl_ReturnsResponseEntityWithShortenResponse() {
        // Arrange
        ShortenRequest request = new ShortenRequest("https://example.com", "abc123");
        ShortenResponse expectedResponse = new ShortenResponse("http://short.ly/abc123");

        when(shortenerUseCase.shorten(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ShortenResponse> response = shortenerController.shortenUrl(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        ShortenResponse body = response.getBody();
        assertNotNull(body); // prevents NPE
        assertEquals("http://short.ly/abc123", body.shortUrl());
        

        verify(shortenerUseCase, times(1)).shorten(request);
    }
}
