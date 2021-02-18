package learning.spring.config;

import learning.spring.binarytea.BinaryTeaProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// 为了演示自动配置的加载，故意不放在learning.spring.binarytea包里
@Configuration
@EnableConfigurationProperties(BinaryTeaProperties.class)
@ConditionalOnProperty(name = "binarytea.ready", havingValue = "true")
public class ShopConfiguration {
}
