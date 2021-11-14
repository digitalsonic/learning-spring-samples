package learning.spring.teamaker.integration;

import learning.spring.teamaker.model.OrderMessage;
import learning.spring.teamaker.model.ProcessResult;
import learning.spring.teamaker.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BinaryteaClient {
    @Autowired
    private OrderService orderService;

    public OrderMessage processOrder(OrderMessage message) {
        Long id = message.getOrderId();
        log.info("开始制作订单{}", id);
        ProcessResult result = orderService.make(id);
        OrderMessage finished = OrderMessage.builder().orderId(id)
                .teaMakerId(result.getTeaMakerId()).state("FINISHED").build();
        log.info("订单{}制作完毕", id);
        return finished;
    }
}
