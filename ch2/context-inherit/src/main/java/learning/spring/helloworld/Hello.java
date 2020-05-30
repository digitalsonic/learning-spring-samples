package learning.spring.helloworld;

public class Hello {
    private String name;

    public String hello() {
        return "Hello World! by " + name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
