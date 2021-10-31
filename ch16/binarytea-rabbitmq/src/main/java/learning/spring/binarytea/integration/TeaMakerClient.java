package learning.spring.binarytea.integration;

import learning.spring.binarytea.model.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TeaMakerClient {
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void notifyPaidOrder(Long id) {
        log.info("发送消息给TeaMaker，通知订单{}", id);
        amqpTemplate.convertAndSend("notify.order.paid", OrderMessage.builder().orderId(id).build());
    }

    @RabbitListener(queues = "notify.order.finished")
    public void receiveFinishedOrder(OrderMessage message) {
        if (OrderStatus.FINISHED.name().equals(message.getState())) {
            applicationEventPublisher.publishEvent(new OrderFinishedEvent(message));
        } else {
            log.warn("被通知到的订单[{}]状态不正确", message.getOrderId());
        }
    }
}
