package learning.spring.customer.integration;

import learning.spring.customer.model.NewOrderForm;
import learning.spring.customer.model.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(contextId = "orderService", name = "binarytea", path = "/order")
public interface OrderService {
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    List<Order> listOrders();

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Order> createNewOrder(@RequestBody NewOrderForm form);
}
