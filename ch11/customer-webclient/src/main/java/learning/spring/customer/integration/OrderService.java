package learning.spring.customer.integration;

import learning.spring.customer.model.NewOrderForm;
import learning.spring.customer.model.Order;
import learning.spring.customer.model.OrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OrderService {
    @Value("${binarytea.order.discount:100}")
    private int discount;
    @Autowired
    private WebClient webClient;

    public Flux<Order> getAllOrders() {
        return webClient.get().uri("/order").accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToFlux(Order.class);
    }

    public Mono<Order> createOrder(Mono<OrderRequest> orderRequest) {
        Mono<NewOrderForm> form = orderRequest.map(r ->
                NewOrderForm.builder().itemIdList(r.getItems())
                        .discount(discount).build());
        return webClient.post().uri("/order")
                .body(form, NewOrderForm.class)
                .retrieve()
                .bodyToMono(Order.class);
    }
}
