package learning.spring.helloworld;

public class SpringHello implements Hello {
    @Override
    public void say() {
        System.out.println("Hello Spring!");
    }
}
