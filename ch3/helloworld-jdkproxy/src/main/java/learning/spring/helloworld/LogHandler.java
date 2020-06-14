package learning.spring.helloworld;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogHandler implements InvocationHandler {
    private Hello source;

    public LogHandler(Hello source) {
        this.source = source;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Ready to say something.");
        try {
            return method.invoke(source, args);
        } finally {
            System.out.println("Already say something.");
        }
    }
}
