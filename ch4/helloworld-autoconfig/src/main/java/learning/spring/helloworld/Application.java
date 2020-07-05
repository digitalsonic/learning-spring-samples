package learning.spring.helloworld;

import learning.spring.speaker.Speaker;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("learning.spring.helloworld")
public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(Application.class);
        Speaker speaker = applicationContext.getBean("speaker", Speaker.class);
        System.out.println(speaker.speak());
    }
}
