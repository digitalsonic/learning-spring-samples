package learning.spring.helloworld;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("learning.spring.helloworld")
public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(Application.class);

        Hello hello = applicationContext.getBean("springHello", Hello.class);
        System.out.println(hello.sayHello(new StringBuffer("My Friend. ")));
        System.out.println(hello.sayHello(new StringBuffer("My Dear Friend. ")));
    }
}
