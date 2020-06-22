package learning.spring.helloworld;

public class SpringHello implements Hello {
    @Override
    public String sayHello(StringBuffer words) {
        return "Hello! " + words;
    }
}
