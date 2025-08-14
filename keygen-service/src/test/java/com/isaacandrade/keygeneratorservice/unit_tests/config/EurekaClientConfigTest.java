package com.isaacandrade.keygeneratorservice.unit_tests.config;

import com.isaacandrade.keygeneratorservice.config.EurekaClientConfig;
import com.netflix.appinfo.InstanceInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.core.env.ConfigurableEnvironment;

import java.lang.reflect.Method;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//  This test class covers the functionality of the EurekaClientConfig class,
// ensuring that it correctlyconfigures the Eureka instance and handles health checks.
class EurekaClientConfigTest {

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
    void eurekaInstanceConfigBean_shouldReturnConfiguredBean() throws Exception {
        when(env.getProperty("port", "8080")).thenReturn("8081");
        when(env.getProperty("server.port", "8081")).thenReturn("8081");

        InetUtils realInetUtils = new InetUtils(new InetUtilsProperties());

        try {
            EurekaInstanceConfigBean bean = eurekaClientConfig.eurekaInstanceConfigBean(realInetUtils);
            assertNotNull(bean);
            assertEquals(8081, bean.getNonSecurePort());
            assertNotNull(bean.getHostname());
            assertNotNull(bean.getIpAddress());
        } catch (UnknownHostException e) {
            // Skip test if hostname is not resolvable in current environment
            System.out.println("Skipping test: Hostname not resolvable: " + e.getMessage());
        }
    }

    @Test
    void get_shouldThrowUnknownHostException_whenHostNotFound() throws Exception {
        Method getMethod = EurekaClientConfig.class.getDeclaredMethod("get", String.class);
        getMethod.setAccessible(true);

        Throwable thrown = assertThrows(UnknownHostException.class, () -> {
            try {
                getMethod.invoke(null, "nonexistent.hostname.example");
            } catch (Exception e) {
                // Unwrap InvocationTargetException
                throw e.getCause();
            }
        });

        assertTrue(thrown.getMessage().contains("Cannot find ip address for hostname"));
    }

    @Test
    void healthCheckHandler_ShouldReturnUP_WhenHealthIsUp() {
        when(healthEndpoint.health()).thenReturn(Health.up().build());

        var handler = eurekaClientConfig.healthCheckHandler(healthEndpoint);
        InstanceInfo.InstanceStatus status = handler.getStatus(InstanceInfo.InstanceStatus.UNKNOWN);

        assertEquals(InstanceInfo.InstanceStatus.UP, status);
    }

    @Test
    void healthCheckHandler_ShouldReturnDOWN_WhenHealthIsDown() {
        when(healthEndpoint.health()).thenReturn(Health.down().build());

        var handler = eurekaClientConfig.healthCheckHandler(healthEndpoint);
        InstanceInfo.InstanceStatus status = handler.getStatus(InstanceInfo.InstanceStatus.UNKNOWN);

        assertEquals(InstanceInfo.InstanceStatus.DOWN, status);
    }
}
