package com.isaacandrade.keygeneratorservice.unit_tests.keygen.application.snowflake.exception;

import org.junit.jupiter.api.Test;

import com.isaacandrade.keygeneratorservice.keygen.application.snowflake.exception.RegressiveClockException;

import static org.junit.jupiter.api.Assertions.*;

// This test class covers the functionality of the RegressiveClockException class
class RegressiveClockExceptionTest {

    @Test
    void testDefaultConstructor_setsDefaultMessage() {
        RegressiveClockException exception = new RegressiveClockException();
        
        assertNotNull(exception.getMessage());
        assertEquals("Incapable To Generate Unique IDs", exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testCustomConstructor_setsCustomMessage() {
        String customMessage = "Custom clock error";
        RegressiveClockException exception = new RegressiveClockException(customMessage);

        assertNotNull(exception.getMessage());
        assertEquals(customMessage, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }
}

