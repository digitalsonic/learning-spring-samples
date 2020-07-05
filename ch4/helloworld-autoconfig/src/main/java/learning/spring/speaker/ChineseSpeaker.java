package learning.spring.speaker;

public class ChineseSpeaker implements Speaker {
    @Override
    public String speak() {
        return "你好，我爱Spring。";
    }
}
