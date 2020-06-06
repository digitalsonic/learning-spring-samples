package learning.spring.helloworld;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(Application.class);

        applicationContext.close();
    }

    @Bean
    public Hello hello() {
        return new Hello();
    }

    @Bean
    public HelloBeanPostProcessor helloBeanPostProcessor() {
        return new HelloBeanPostProcessor();
    }
}
