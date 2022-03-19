package learning.spring.customer.web;

import learning.spring.customer.integration.OrderService;
import learning.spring.customer.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class OrderRouterConfig {
    @Autowired
    private OrderService orderService;

    @Bean
    public RouterFunction<?> orderRouter() {
        return route()
                .GET("/order",
                        RequestPredicates.accept(MediaType.APPLICATION_JSON),
                        request -> ok().body(orderService.getAllOrders(), Order.class))
                .POST("/order", orderHandler()::createNewOrder)
                .build();
    }

    @Bean
    public OrderHandler orderHandler() {
        return new OrderHandler();
    }
}
