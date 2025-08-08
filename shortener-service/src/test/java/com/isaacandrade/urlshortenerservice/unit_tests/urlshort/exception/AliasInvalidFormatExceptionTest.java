package com.isaacandrade.urlshortenerservice.unit_tests.urlshort.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.isaacandrade.urlshortenerservice.urlshort.exception.AliasInvalidFormatException;

public class AliasInvalidFormatExceptionTest {
    
    @Test
    void testDefaultConstructorMessage() {
        AliasInvalidFormatException exception = new AliasInvalidFormatException();
        assertEquals("Invalid Alias Format", exception.getMessage());
    }

    @Test
    void testCustomMessageConstructor() {
        String customMessage = "Alias must be alphanumeric and 5-10 characters long";
        AliasInvalidFormatException exception = new AliasInvalidFormatException(customMessage);
        assertEquals(customMessage, exception.getMessage());
    }

    @Test
    void testExceptionIsRuntimeException() {
        AliasInvalidFormatException exception = new AliasInvalidFormatException();
        assertTrue(exception instanceof RuntimeException);
    }
}
