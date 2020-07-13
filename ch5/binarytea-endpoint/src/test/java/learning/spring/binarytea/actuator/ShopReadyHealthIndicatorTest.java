package learning.spring.binarytea.actuator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ShopReadyHealthIndicatorTest {
    @Autowired
    private HealthContributorRegistry registry;

    @Test
    void testRegistryContainsShopReady() {
        assertNotNull(registry.getContributor("shopReady"));
    }
}