package com.isaacandrade.keygeneratorservice.unit_tests.keygen.application.snowflake.exception.message;

import com.isaacandrade.keygeneratorservice.keygen.application.snowflake.exception.NetworkException;
import com.isaacandrade.keygeneratorservice.keygen.application.snowflake.exception.message.NetworkErrorMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class NetworkErrorMessageTests {

    @Mock
    NetworkErrorMessage message;

    @Test
    void constructorAndGetters_shouldWorkCorrectly() {
        message = new NetworkErrorMessage(500, "Network Error");
        assertEquals(500, message.getStatus());
        assertEquals("Network Error", message.getMessage());
    }

    @Test
    void setters_shouldUpdateFieldsCorrectly() {
        message = new NetworkErrorMessage(0, null);
        message.setStatus(500);
        message.setMessage("Internal Error");

        assertEquals(500, message.getStatus());
        assertEquals("Internal Error", message.getMessage());
    }

    // Test for default constructor
    @Test
    void defaultConstructor_shouldSetCorrectMessage() {
        NetworkException exception = new NetworkException();

        assertNotNull(exception.getMessage());
        assertEquals("Network Error!", exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void setStatus_shouldUpdateStatusField() {
        
        message = new NetworkErrorMessage(0, null);
        int newStatus = 500;

        message.setStatus(newStatus);

        assertEquals(newStatus, message.getStatus(), "Status should be updated to the value set");
    }

    @Test
    void setMessage_shouldUpdateMessageField() {
        
        message = new NetworkErrorMessage(0, null);
        String newMessage = "Service temporarily unavailable";

        message.setMessage(newMessage);

        assertEquals(newMessage, message.getMessage(), "Message should be updated to the value set");
    }
}
