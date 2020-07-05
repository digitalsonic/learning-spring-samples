package learning.spring.helloworld;

import learning.spring.speaker.SpeakerBeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/application.properties")
public class AutoConfiguration {
    @Bean
    public static SpeakerBeanFactoryPostProcessor speakerBeanFactoryPostProcessor() {
        return new SpeakerBeanFactoryPostProcessor();
    }
}
