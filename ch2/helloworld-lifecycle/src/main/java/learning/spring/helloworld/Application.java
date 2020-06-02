package learning.spring.helloworld;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
    private ClassPathXmlApplicationContext applicationContext;

    public static void main(String[] args) {
        Application application = new Application();
        application.sayHello();
        application.close();
    }

    public Application() {
        applicationContext = new ClassPathXmlApplicationContext("beans.xml");
    }

    public void sayHello() {
        Hello hello = (Hello) applicationContext.getBean("hello");
        System.out.println(hello.hello());
    }

    public void close() {
        applicationContext.close();
    }
}
