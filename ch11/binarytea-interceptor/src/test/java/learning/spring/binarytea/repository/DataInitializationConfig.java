package learning.spring.binarytea.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "data.init.enable", havingValue = "true")
public class DataInitializationConfig {
    @Bean
    public DataInitializationRunner dataInitializationRunner() {
        return new DataInitializationRunner();
    }
}
