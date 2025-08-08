package com.isaacandrade.urlshortenerservice.unit_tests.urlshort.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.isaacandrade.urlshortenerservice.urlshort.exception.KeygenServiceUnvailableException;

class KeygenServiceUnvailableExceptionTest {

    @Test
    void testDefaultConstructorMessage() {
        KeygenServiceUnvailableException exception = new KeygenServiceUnvailableException();
        assertEquals("Keygen Service Is Unavailable", exception.getMessage());
    }

    @Test
    void testCustomMessageConstructor() {
        String customMessage = "Keygen service failed due to timeout.";
        KeygenServiceUnvailableException exception = new KeygenServiceUnvailableException(customMessage);
        assertEquals(customMessage, exception.getMessage());
    }

    @Test
    void testExceptionIsRuntimeException() {
        KeygenServiceUnvailableException exception = new KeygenServiceUnvailableException();
        assertTrue(exception instanceof RuntimeException);
    }
}

