package learning.spring.customer;

import learning.spring.customer.integration.OrderService;
import learning.spring.customer.model.NewOrderForm;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(5)
@Setter
@Slf4j
public class OrderRunner implements ApplicationRunner {
    @Autowired
    private OrderService orderService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<learning.spring.customer.model.Order> orders = orderService.listOrders();
        log.info("调用前的订单数量: {}", orders.size());
        NewOrderForm form = NewOrderForm.builder()
                .itemIdList(Arrays.asList("1"))
                .discount(90).build();
        ResponseEntity<learning.spring.customer.model.Order> response =
                orderService.createNewOrder(form);
        log.info("HTTP Status: {}, Headers: {}", response.getStatusCode(), response.getHeaders());
        log.info("Body: {}", response.getBody());
    }
}
