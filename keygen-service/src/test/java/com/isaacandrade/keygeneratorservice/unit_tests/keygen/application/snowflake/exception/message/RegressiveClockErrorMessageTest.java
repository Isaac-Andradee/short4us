package com.isaacandrade.keygeneratorservice.unit_tests.keygen.application.snowflake.exception.message;

import org.junit.jupiter.api.Test;

import com.isaacandrade.keygeneratorservice.keygen.application.snowflake.exception.message.RegressiveClockErrorMessage;

import static org.junit.jupiter.api.Assertions.*;

// This test class covers the functionality of the RegressiveClockErrorMessage class
class RegressiveClockErrorMessageTest {

    @Test
    void constructor_shouldInitializeFieldsCorrectly() {
       
        int expectedStatus = 409;
        String expectedMessage = "Clock moved backwards";

        RegressiveClockErrorMessage errorMessage = new RegressiveClockErrorMessage(expectedStatus, expectedMessage);

        assertEquals(expectedStatus, errorMessage.getStatus());
        assertEquals(expectedMessage, errorMessage.getMessage());
    }

    @Test
    void setters_shouldUpdateFieldsCorrectly() {
        
        RegressiveClockErrorMessage errorMessage = new RegressiveClockErrorMessage(0, "");

        errorMessage.setStatus(500);
        errorMessage.setMessage("System time error");

        assertEquals(500, errorMessage.getStatus());
        assertEquals("System time error", errorMessage.getMessage());
    }

    @Test
    void getters_shouldReturnUpdatedValues() {
        RegressiveClockErrorMessage errorMessage = new RegressiveClockErrorMessage(404, "Not Found");

        assertEquals(404, errorMessage.getStatus());
        assertEquals("Not Found", errorMessage.getMessage());
    }
}

