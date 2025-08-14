package com.isaacandrade.keygeneratorservice.unit_tests.keygen.infra.router;

import com.isaacandrade.keygeneratorservice.keygen.application.KeyGenUseCase;
import com.isaacandrade.keygeneratorservice.keygen.infra.router.KeyGenController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Unit tests for KeyGenController 
class KeyGenControllerTest {

    private KeyGenUseCase keyGenUseCase;
    private KeyGenController keyGenController;

    @BeforeEach
    void setUp() {
        keyGenUseCase = mock(KeyGenUseCase.class);
        keyGenController = new KeyGenController(keyGenUseCase);
    }

    @Test
    void testGenerate_ReturnsUniqueKeyInResponse() {
        // Given
        String expectedKey = "abc123";
        when(keyGenUseCase.generateUniqueKey()).thenReturn(expectedKey);

        // When
        ResponseEntity<Map<String, String>> response = keyGenController.generate();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("shortKey"));
        assertEquals(expectedKey, response.getBody().get("shortKey"));

        // Verify interaction
        verify(keyGenUseCase, times(1)).generateUniqueKey();
        verifyNoMoreInteractions(keyGenUseCase);
    }
}

