package learning.spring.helloworld;

public class DefaultGoodBye implements GoodBye {
    @Override
    public String sayBye() {
        return "Bye! ";
    }
}
