package learning.spring.config;

import learning.spring.binarytea.BinaryTeaApplication;
import learning.spring.binarytea.BinaryTeaProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(classes = BinaryTeaApplication.class, properties = {
        "binarytea.ready=true",
        "binarytea.open-hours=8:30-22:00"
})
public class ShopConfigurationEnableTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testPropertiesBeanAvailable() {
        assertNotNull(applicationContext.getBean(BinaryTeaProperties.class));
        assertTrue(applicationContext
                .containsBean("binarytea-learning.spring.binarytea.BinaryTeaProperties"));
    }

    @Test
    void testPropertyValues() {
        BinaryTeaProperties properties =
                applicationContext.getBean(BinaryTeaProperties.class);
        assertTrue(properties.isReady());
        assertEquals("8:30-22:00", properties.getOpenHours());
    }
}