package learning.spring.helloworld;

import org.springframework.stereotype.Component;

@Component
public class Hello {
    public String hello() {
        return "Hello World!";
    }
}
