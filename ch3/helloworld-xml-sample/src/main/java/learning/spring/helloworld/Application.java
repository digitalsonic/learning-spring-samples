package learning.spring.helloworld;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        Hello hello = applicationContext.getBean("springHello", Hello.class);
        System.out.println(hello.sayHello(new StringBuffer("My Friend. ")));
        System.out.println(hello.sayHello(new StringBuffer("My Dear Friend. ")));
    }
}
