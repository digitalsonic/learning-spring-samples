package learning.spring.helloworld;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CustomEventListener {
    @EventListener
    public void onEvent(CustomEvent customEvent) {
        System.out.println("CustomEvent Source: " + customEvent.getSource());
    }
}
