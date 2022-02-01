package learning.spring.helloworld;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        Application application = new Application();
        application.sayHello();
    }

    public Application() {
        applicationContext = new ClassPathXmlApplicationContext("beans.xml");
    }

    public void sayHello() {
        Hello hello = applicationContext.getBean("hello", Hello.class);
        System.out.println(hello.hello());
    }
}
