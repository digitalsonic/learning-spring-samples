package learning.spring.helloworld;

import org.aspectj.lang.ProceedingJoinPoint;

public class SayAspect {
    private int counter = 0;

    public void countSentence(StringBuffer words) {
        words.append("[" + ++counter + "]\n");
    }

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

