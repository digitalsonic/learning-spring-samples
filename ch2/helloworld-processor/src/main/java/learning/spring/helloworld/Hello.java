package learning.spring.helloworld;

import javax.annotation.PostConstruct;

public class Hello {
    @PostConstruct
    public void init() {
        System.out.println("Hello PostConstruct");
    }

    public String hello() {
        return "Hello World!";
    }
}
