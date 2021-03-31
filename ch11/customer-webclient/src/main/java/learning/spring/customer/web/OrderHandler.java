package learning.spring.customer.web;

import learning.spring.customer.integration.OrderService;
import learning.spring.customer.model.Order;
import learning.spring.customer.model.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class OrderHandler {
    @Autowired
    private OrderService orderService;

    public Mono<ServerResponse> createNewOrder(ServerRequest request) {
        Mono<OrderRequest> orderRequest = request.bodyToMono(OrderRequest.class);
        Mono<Order> order = orderService.createOrder(orderRequest);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(order, Order.class);
    }
}
