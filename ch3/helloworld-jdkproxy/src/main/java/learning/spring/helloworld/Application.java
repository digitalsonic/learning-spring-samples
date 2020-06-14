package learning.spring.helloworld;

import java.lang.reflect.Proxy;

public class Application {
    public static void main(String[] args) {
        Hello original = new SpringHello();
        Hello target = (Hello) Proxy.newProxyInstance(Hello.class.getClassLoader(),
                original.getClass().getInterfaces(), new LogHandler(original));
        target.say();
    }
}
