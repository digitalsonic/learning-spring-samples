package learning.spring.helloworld;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.DisposableBean;

public class Hello implements DisposableBean {
    public String hello() {
        return "Hello World!";
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("destroy()");
    }

    public void close() {
        System.out.println("close()");
    }

    @PreDestroy
    public void shutdown() {
        System.out.println("shutdown()");
    }
}
