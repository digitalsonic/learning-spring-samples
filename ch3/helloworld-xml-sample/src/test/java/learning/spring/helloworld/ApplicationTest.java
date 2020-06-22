package learning.spring.helloworld;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ApplicationTest {
    @Autowired
    private Hello hello;
    @Autowired
    private SayAspect sayAspect;

    @BeforeEach
    public void setUp() {
        // Spring容器是同一个，因此SayAspect也是同一个
        // 重置计数器，方便进行断言判断
        sayAspect.reset();
    }

    @Test
    @DisplayName("springHello不为空")
    public void testNotEmpty() {
        assertNotNull(hello);
    }

    @Test
    @DisplayName("springHello是否为GoodBye类型")
    public void testIntroduction() {
        assertTrue(hello instanceof GoodBye);
    }

    @Test
    @DisplayName("通知是否均已执行")
    public void testAdvice() {
        StringBuffer words = new StringBuffer("Test. ");
        String sentence = hello.sayHello(words);
        assertEquals("Test. Welcome to Spring! [1]\n", words.toString());
        assertEquals("Hello! Test. Welcome to Spring! [1]\nBye! ", sentence);
    }

    @Test
    @DisplayName("说两句话，检查计数")
    public void testMultipleSpeaking() {
        assertEquals("Hello! Test. Welcome to Spring! [1]\nBye! ",
                hello.sayHello(new StringBuffer("Test. ")));
        assertEquals("Hello! Test. Welcome to Spring! [2]\nBye! ",
                hello.sayHello(new StringBuffer("Test. ")));
    }
}
