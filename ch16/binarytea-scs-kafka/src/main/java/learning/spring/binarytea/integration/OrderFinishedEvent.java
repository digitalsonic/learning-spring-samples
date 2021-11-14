package learning.spring.binarytea.integration;

import org.springframework.context.ApplicationEvent;

public class OrderFinishedEvent extends ApplicationEvent {
    public OrderFinishedEvent(OrderMessage source) {
        super(source);
    }
}
