package learning.spring.helloworld;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class HelloAspect {
    @Before("target(learning.spring.helloworld.Hello) && args(words)")
    public void addWords(StringBuffer words) {
        words.append("Welcome to Spring! ");
    }
}
