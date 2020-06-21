package learning.spring.helloworld;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(2)
public class SayAspect {
    @DeclareParents(value = "learning.spring.helloworld.*",
            defaultImpl = DefaultGoodBye.class)
    private GoodBye bye;
    private int counter = 0;

    @Before("execution(* say*(..)) && args(words)")
    public void countSentence(StringBuffer words) {
        words.append("[" + ++counter + "]\n");
    }

    @Around("execution(* sayHello(..)) && this(bye)")
    public String addSay(ProceedingJoinPoint pjp, GoodBye bye) throws Throwable {
        return pjp.proceed() + bye.sayBye();
    }

    public void reset() {
        counter = 0;
    }

    public int getCounter() {
        return counter;
    }
}

