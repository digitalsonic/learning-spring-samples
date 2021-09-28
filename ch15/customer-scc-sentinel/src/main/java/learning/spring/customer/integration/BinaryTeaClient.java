package learning.spring.customer.integration;

import learning.spring.customer.model.Order;
import learning.spring.customer.support.OrderNotFinishedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class BinaryTeaClient {
    @Autowired
    private OrderService orderService;
    private CircuitBreaker orderCheckerCircuitBreaker;

    public BinaryTeaClient(CircuitBreakerFactory circuitBreakerFactory) {
        orderCheckerCircuitBreaker = circuitBreakerFactory.create("order-checker");
    }

    public boolean isOrderFinished(Long id) {
        return orderCheckerCircuitBreaker.run(() -> {
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
        }, t -> false);
    }
}
