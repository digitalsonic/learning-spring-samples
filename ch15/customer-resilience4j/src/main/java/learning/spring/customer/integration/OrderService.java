package learning.spring.customer.integration;

import learning.spring.customer.model.NewOrderForm;
import learning.spring.customer.model.Order;
import learning.spring.customer.model.StatusForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(contextId = "orderService", name = "binarytea", path = "/order")
public interface OrderService {
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    List<Order> listOrders();

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Order> createNewOrder(@RequestBody NewOrderForm form);

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Order> modifyOrderStatus(@RequestBody StatusForm form);

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Order> queryOneOrder(@PathVariable("id") Long id);
}
