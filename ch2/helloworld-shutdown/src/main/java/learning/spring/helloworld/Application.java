package learning.spring.helloworld;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(Application.class);
        applicationContext.start(); // 这会触发Lifecycle的start()
        Hello hello = applicationContext.getBean("hello", Hello.class);
        System.out.println(hello.hello());
        applicationContext.close(); // 这会触发Lifecycle的stop()
        System.out.println(hello.hello());
    }

    @Bean
    public Hello hello() {
        return new Hello();
    }
}
