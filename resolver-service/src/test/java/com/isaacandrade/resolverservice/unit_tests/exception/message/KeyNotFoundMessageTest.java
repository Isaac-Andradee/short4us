package com.isaacandrade.resolverservice.unit_tests.exception.message;

import com.isaacandrade.resolverservice.exception.KeyNotFoundException;
import com.isaacandrade.resolverservice.exception.message.KeyNotFoundMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class KeyNotFoundMessageTest {

    @Test
    void shouldCreateKeyNotFoundMessage() {
        KeyNotFoundMessage msg = new KeyNotFoundMessage(404, "Not found");
        assertEquals(404, msg.getStatus());
        assertEquals("Not found", msg.getMessage());
        msg.setStatus(400);
        msg.setMessage("Changed");
        assertEquals(400, msg.getStatus());
        assertEquals("Changed", msg.getMessage());
    }
    @Test
    void shouldHaveDefaultMessage() {
        KeyNotFoundException ex = new KeyNotFoundException();
        assertEquals("Key Not Found", ex.getMessage());
    }
}
