package learning.spring.customer.integration;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import learning.spring.customer.model.Order;
import learning.spring.customer.support.OrderNotFinishedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class BinaryTeaClient {
    @Autowired
    private OrderService orderService;

    @CircuitBreaker(name = "order-checker", fallbackMethod = "orderIsNotFinished")
    public boolean isOrderFinished(Long id) {
        ResponseEntity<Order> entity = orderService.queryOneOrder(id);
        if (HttpStatus.NOT_FOUND == entity.getStatusCode()) {
            throw new IllegalArgumentException("没找到订单");
        }
        Order order = entity.getBody();
        if ("FINISHED".equalsIgnoreCase(order.getStatus())) {
            return true;
        }
        log.info("订单{}还没好", id);
        throw new OrderNotFinishedException();
    }

    public boolean orderIsNotFinished(Long id, Exception e) {
        return false;
    }
}
