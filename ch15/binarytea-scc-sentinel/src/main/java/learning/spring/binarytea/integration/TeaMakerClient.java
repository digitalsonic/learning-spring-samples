package learning.spring.binarytea.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class TeaMakerClient {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${tea-maker.url}")
    private String teaMakerUrl;
    private CircuitBreaker circuitBreaker;

    public TeaMakerClient(CircuitBreakerFactory factory) {
        circuitBreaker = factory.create("tea-maker");
    }

    public TeaMakerResult makeTea(Long id) {
        return circuitBreaker.run(() -> {
            ResponseEntity<TeaMakerResult> entity =
                    restTemplate.postForEntity(teaMakerUrl + "/order/{id}", null,
                            TeaMakerResult.class, id);
            log.info("请求TeaMaker，响应码：{}", entity.getStatusCode());
            if (entity.getStatusCode() == HttpStatus.BAD_REQUEST) {
                return null;
            }
            return entity.getBody();
        }, t -> notFinished(id, t));
    }

    public TeaMakerResult notFinished(Long id, Throwable t) {
        log.warn("Fallback by Sentinel - {}", t.getMessage());
        TeaMakerResult result = new TeaMakerResult();
        result.setFinish(false);
        result.setOrderId(id);
        return result;
    }
}
