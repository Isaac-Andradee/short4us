package com.isaacandrade.urlshortenerservice.unit_tests.urlshort.application;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.isaacandrade.urlshortenerservice.urlshort.application.AliasValidator;

public class AliasValidatorTest {

    private AliasValidator aliasValidator;

    @BeforeEach
    void setUp() {
        aliasValidator = Mockito.mock(AliasValidator.class);
    }

    @Test
    void testAliasValidator() {
        String alias = "alias";

        aliasValidator.validate(alias);

        verify(aliasValidator, times(1)).validate(alias);
    }
    
}
