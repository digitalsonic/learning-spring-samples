package learning.spring.customer.integration;

import learning.spring.customer.model.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@Service
public class MenuService {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${binarytea.url}")
    private String binarytea;

    public Flux<MenuItem> getAllMenu() {
        ParameterizedTypeReference<List<MenuItem>> typeReference =
                new ParameterizedTypeReference<List<MenuItem>>() {
                };
        URI uri = UriComponentsBuilder.fromUriString(binarytea + "/menu").build().toUri();
        RequestEntity<Void> request = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).build();
        ResponseEntity<List<MenuItem>> response = restTemplate.exchange(request, typeReference);
        return Flux.fromIterable(response.getBody());
    }

    public Mono<MenuItem> getById(Long id) {
        MenuItem item = restTemplate.getForObject(binarytea + "/menu/{id}", MenuItem.class, id);
        return item != null ? Mono.just(item) : Mono.empty();
    }
}
