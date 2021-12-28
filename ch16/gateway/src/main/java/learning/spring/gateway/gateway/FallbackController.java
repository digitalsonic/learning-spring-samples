package learning.spring.gateway.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RestController
@Slf4j
public class FallbackController {
    @RequestMapping("/fallback")
    public Mono<String> fallback(ServerHttpRequest request) {
        request.getHeaders().forEach((k, v) -> log.info("Header [{}] : {}",
                k, v.stream().collect(Collectors.joining(","))));
        return Mono.just("{}");
    }
}
