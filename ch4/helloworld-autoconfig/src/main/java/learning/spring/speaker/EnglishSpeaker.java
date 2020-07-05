package learning.spring.speaker;

public class EnglishSpeaker implements Speaker {
    @Override
    public String speak() {
        return "Hello, I love Spring.";
    }
}
