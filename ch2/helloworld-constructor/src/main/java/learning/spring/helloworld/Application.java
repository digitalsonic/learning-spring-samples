package learning.spring.helloworld;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
    private ClassPathXmlApplicationContext context;

    public static void main(String[] args) {
        new Application().sayHello();
    }

    public Application() {
        context = new ClassPathXmlApplicationContext("beans.xml");
    }

    public void sayHello() {
        context.getBean("hello", Hello.class).hello();
    }
}
