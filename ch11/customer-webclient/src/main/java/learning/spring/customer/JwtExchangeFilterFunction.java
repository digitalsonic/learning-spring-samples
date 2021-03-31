package learning.spring.customer;

import learning.spring.customer.model.TokenRequest;
import learning.spring.customer.model.TokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Slf4j
public class JwtExchangeFilterFunction implements ExchangeFilterFunction {
    @Value("${jwt.username}")
    private String username;
    @Value("${jwt.password}")
    private String password;
    @Value("${binarytea.url}")
    private String binarytea;
    private WebClient webClient;
    private String token;

    public JwtExchangeFilterFunction(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @PostConstruct
    public void initToken() {
        ResponseEntity<TokenResponse> entity = webClient.post().uri(binarytea + "/token")
                .bodyValue(new TokenRequest(username, password))
                .retrieve()
                .toEntity(TokenResponse.class)
                .timeout(Duration.ofSeconds(1)).block();

        if (HttpStatus.OK == entity.getStatusCode() && entity.getBody() != null) {
            token = entity.getBody().getToken();
            log.info("获得Token：{}", token);
        } else {
            log.warn("获取Token失败，原因：{}", entity.getBody());
        }
    }

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        if (StringUtils.isBlank(token)) {
            initToken();
        }
        if (StringUtils.isBlank(token) ||
                request.headers().containsKey(HttpHeaders.AUTHORIZATION)) {
            return next.exchange(request);
        }
        ClientRequest filtered = ClientRequest.from(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .build();
        return next.exchange(filtered);
    }
}
