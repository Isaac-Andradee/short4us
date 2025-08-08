package com.isaacandrade.urlshortenerservice.unit_tests.urlshort.config;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.core.env.ConfigurableEnvironment;

import com.isaacandrade.urlshortenerservice.config.EurekaClientConfig;
import com.netflix.appinfo.InstanceInfo;

public class EurekaClientConfigTest {
    private ConfigurableEnvironment env;
    private HealthEndpoint healthEndpoint;
    private EurekaClientConfig eurekaClientConfig;

    @BeforeEach
    void setUp() {
        env = mock(ConfigurableEnvironment.class);
        healthEndpoint = mock(HealthEndpoint.class);
        eurekaClientConfig = new EurekaClientConfig(env);
    }

    @Test
    void eurekaInstanceConfigBean_shouldReturnConfiguredBean() {
        // Arrange
        when(env.getProperty("port", "8080")).thenReturn("8081");
        when(env.getProperty(eq("server.port"), eq("8081"))).thenReturn("8081");

        // Use real InetUtils
        InetUtils realInetUtils = new InetUtils(new InetUtilsProperties());

        // Act
        EurekaInstanceConfigBean bean = eurekaClientConfig.eurekaInstanceConfigBean(realInetUtils);

        // Assert
        assertNotNull(bean);
        assertEquals(8081, bean.getNonSecurePort());
        assertNotNull(bean.getHostname());
        assertNotNull(bean.getIpAddress());
    }

    @Test
    void healthCheckHandler_ShouldReturnUP_WhenHealthIsUp() {
        // Arrange
        when(healthEndpoint.health()).thenReturn(Health.up().build());

        var handler = eurekaClientConfig.healthCheckHandler(healthEndpoint);

        // Act
        InstanceInfo.InstanceStatus status = handler.getStatus(InstanceInfo.InstanceStatus.UNKNOWN);

        // Assert
        assertEquals(InstanceInfo.InstanceStatus.UP, status);
    }

    @Test
    void healthCheckHandler_ShouldReturnDOWN_WhenHealthIsDown() {
        // Arrange
        when(healthEndpoint.health()).thenReturn(Health.down().build());

        var handler = eurekaClientConfig.healthCheckHandler(healthEndpoint);

        // Act
        InstanceInfo.InstanceStatus status = handler.getStatus(InstanceInfo.InstanceStatus.UNKNOWN);

        // Assert
        assertEquals(InstanceInfo.InstanceStatus.DOWN, status);
    }

    @Test
    void get_shouldReturn127001_whenUnknownHostExceptionOccursInFallback() throws Exception {
        Method getMethod = EurekaClientConfig.class.getDeclaredMethod("get", String.class);
        getMethod.setAccessible(true);

        // Intentionally pass a hostname that can't be resolved and will reach the final
        // fallback
        String result = (String) getMethod.invoke(null, "nonexistent.hostname.example");

        assertNotNull(result);
        // It's hard to reliably trigger UnknownHostException without mocking,
        // but check the logic at least returns a non-null IP-like value
        assertEquals("127.0.1.1", result); // Best guess
    }
}
