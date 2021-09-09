package learning.spring.binarytea.notify;

import learning.spring.binarytea.integration.TeaMakerClient;
import learning.spring.binarytea.integration.TeaMakerResult;
import learning.spring.binarytea.model.Order;
import learning.spring.binarytea.model.OrderStatus;
import learning.spring.binarytea.repository.OrderRepository;
import learning.spring.binarytea.repository.TeaMakerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class TeaMakerNotifier {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TeaMakerRepository teaMakerRepository;
    @Autowired
    private TeaMakerClient teaMakerClient;

    @Scheduled(fixedDelay=2000)
    public void notifyTeaMaker() {
        // 没考虑并发执行的问题
        List<Order> orders = orderRepository.findByStatusOrderById(OrderStatus.PAID);
        for (Order o : orders) {
            try {
                notifyOneOrder(o);
            } catch(Exception e) {
                log.error("通知处理订单失败", e);
            }
        }
    }

    private void notifyOneOrder(Order o) {
        TeaMakerResult result = teaMakerClient.makeTea(o.getId());
        if (result == null || !result.isFinish()) {
            return;
        }
        teaMakerRepository.findById(result.getTeaMakerId()).ifPresent(o::setMaker);
        o.setStatus(OrderStatus.FINISHED);
        orderRepository.save(o);
    }
}
