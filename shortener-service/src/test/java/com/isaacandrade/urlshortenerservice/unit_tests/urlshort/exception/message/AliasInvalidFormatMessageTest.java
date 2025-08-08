package com.isaacandrade.urlshortenerservice.unit_tests.urlshort.exception.message;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import com.isaacandrade.urlshortenerservice.urlshort.exception.message.AliasInvalidFormatMessage;

public class AliasInvalidFormatMessageTest {

    @Test
    void testAliasInvalidFormatMessage() {
        int status = 400;
        String message = "Invalid Formart Message!";

        AliasInvalidFormatMessage invalid = new AliasInvalidFormatMessage(status, message);

        assertEquals(status, invalid.getStatus());
        assertEquals(message, invalid.getMessage());
    }

    @Test
    void testSetters() {
        AliasInvalidFormatMessage invalid = new AliasInvalidFormatMessage(400, "Invalid Formart");

        invalid.setMessage("Invalid Formart");
        invalid.setStatus(400);

        assertEquals(400, invalid.getStatus());
        assertEquals("Invalid Formart", invalid.getMessage());

    }
}
