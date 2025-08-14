package com.isaacandrade.urlshortenerservice.unit_tests.urlshort.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import com.isaacandrade.urlshortenerservice.config.WebClientConfig;

public class WebClientConfigTest {

    @Test
    void webClientBuild_shouldReturnWebClientBuilder() {
        //arrange
        WebClientConfig config = new WebClientConfig();

        //act
        WebClient.Builder builder = config.webClientBuild();

        //assert
        assertNotNull(builder, "WebClient.Builder should not be null");
        assertNotNull(builder.build(), "WebClient built instance should not be null");
    }
    
}
