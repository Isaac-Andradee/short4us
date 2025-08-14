package com.isaacandrade.urlshortenerservice.unit_tests.urlshort.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;import org.junit.jupiter.api.Test;

import com.isaacandrade.urlshortenerservice.config.OpenApiConfig;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

public class OpenApiConfigTest {
    
    @Test
    void customOpenAPI_shouldReturnConfiguredOpenAPI() {
        // Arrange
        OpenApiConfig config = new OpenApiConfig();

        // Act
        OpenAPI openAPI = config.customOpenAPI();

        // Assert
        assertNotNull(openAPI, "OpenAPI object should not be null");

        Info info = openAPI.getInfo();
        assertNotNull(info, "Info section should not be null");
        assertEquals("Shortener Service Doc", info.getTitle());
        assertEquals("Shortener Service API", info.getDescription());
        assertEquals("v1.0.0", info.getVersion());
        assertEquals("/", info.getTermsOfService());

        License license = info.getLicense();
        assertNotNull(license, "License section should not be null");
        assertEquals("Apache 2.0", license.getName());
        assertEquals("https://github.com/Isaac-Andradee/url-shortener/blob/main/LICENSE", license.getUrl());
    }
}
