package learning.spring.customer.integration;

import learning.spring.customer.model.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class MenuService {
    @Autowired
    private WebClient webClient;

    public Flux<MenuItem> getAllMenu() {
        return webClient.get().uri("/menu").accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToFlux(MenuItem.class).timeout(Duration.ofSeconds(1));
    }

    public Mono<MenuItem> getById(Long id) {
        return webClient.get().uri("/menu/{id}", id)
                .retrieve().bodyToMono(MenuItem.class).timeout(Duration.ofSeconds(1));
    }
}
