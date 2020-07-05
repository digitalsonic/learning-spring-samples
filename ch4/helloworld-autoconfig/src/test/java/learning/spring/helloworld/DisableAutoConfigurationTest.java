package learning.spring.helloworld;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringJUnitConfig(AutoConfiguration.class)
@TestPropertySource(properties = {
        "spring.speaker.enable=false"
})
public class DisableAutoConfigurationTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testHasNoSpeaker() {
        assertFalse(applicationContext.containsBean("speaker"));
    }
}
