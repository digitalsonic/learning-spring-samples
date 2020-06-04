package learning.spring.helloworld;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(Application.class);
        Hello hello = applicationContext.getBean("hello", Hello.class);
        System.out.println(hello.hello());

        applicationContext.getBean("customEventPublisher", CustomEventPublisher.class)
                .fire();

        applicationContext.close();
    }
}
