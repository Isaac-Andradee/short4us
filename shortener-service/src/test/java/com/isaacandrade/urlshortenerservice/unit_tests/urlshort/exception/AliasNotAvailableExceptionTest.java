package com.isaacandrade.urlshortenerservice.unit_tests.urlshort.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.isaacandrade.urlshortenerservice.urlshort.exception.AliasNotAvailableException;

class AliasNotAvailableExceptionTest {

    @Test
    void testDefaultConstructorMessage() {
        AliasNotAvailableException exception = new AliasNotAvailableException();
        assertEquals("Alias Not Available", exception.getMessage());
    }

    @Test
    void testCustomMessageConstructor() {
        String customMessage = "Alias xyz123 is already taken.";
        AliasNotAvailableException exception = new AliasNotAvailableException(customMessage);
        assertEquals(customMessage, exception.getMessage());
    }

    @Test
    void testExceptionIsRuntimeException() {
        AliasNotAvailableException exception = new AliasNotAvailableException();
        assertTrue(exception instanceof RuntimeException);
    }
}
