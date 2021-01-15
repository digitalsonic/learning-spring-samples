package learning.spring.config;

import learning.spring.binarytea.BinaryTeaApplication;
import learning.spring.binarytea.actuator.ShopReadyHealthIndicator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(classes = BinaryTeaApplication.class, properties = {
        "binarytea.ready=false"
})
public class ShopConfigurationDisableTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testPropertiesBeanUnavailable() {
        assertEquals("false",
                applicationContext.getEnvironment().getProperty("binarytea.ready"));
        assertFalse(applicationContext
                .containsBean("binarytea-learning.spring.binarytea.BinaryTeaProperties"));
    }

    @Test
    void testIndicatorDown() {
        ShopReadyHealthIndicator indicator = applicationContext.getBean(ShopReadyHealthIndicator.class);
        assertEquals(Status.DOWN, indicator.getHealth(false).getStatus());
    }
}
