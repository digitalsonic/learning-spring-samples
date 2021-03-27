package learning.spring.customer.integration;

import learning.spring.customer.model.NewOrderForm;
import learning.spring.customer.model.Order;
import learning.spring.customer.model.OrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@Service
@Slf4j
public class OrderService {
    @Value("${binarytea.order.discount:100}")
    private int discount;
    @Value("${binarytea.url}")
    private String binarytea;
    @Autowired
    private RestTemplate restTemplate;

    public Flux<Order> getAllOrders() {
        ParameterizedTypeReference<List<Order>> typeReference =
                new ParameterizedTypeReference<>() {};
        RequestEntity<Void> request = RequestEntity.get(URI.create(binarytea + "/order"))
                .accept(MediaType.APPLICATION_JSON).build();
        ResponseEntity<List<Order>> response = restTemplate.exchange(request, typeReference);
        if (response.getStatusCode().is2xxSuccessful()) {
            return Flux.fromIterable(response.getBody());
        }
        return Flux.empty();
    }

    public Mono<Order> createOrder(Mono<OrderRequest> orderRequest) {
        return orderRequest
                .map(r -> NewOrderForm.builder().itemIdList(r.getItems()).discount(discount).build())
                .map(f -> restTemplate.postForEntity(binarytea + "/order", f, Order.class))
                .filter(e -> e.getStatusCode().is2xxSuccessful())
                .map(e -> e.getBody())
                .log();
    }
}
