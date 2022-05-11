package learning.spring.customer;

import learning.spring.customer.model.TokenRequest;
import learning.spring.customer.model.TokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateRequestCustomizer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Slf4j
public class JwtClientHttpRequestInitializer implements RestTemplateRequestCustomizer {
    @Autowired
    private ClientHttpRequestFactory requestFactory;
    @Value("${jwt.username}")
    private String username;
    @Value("${jwt.password}")
    private String password;
    @Value("${binarytea.url}")
    private String binarytea;
    private String token;

    @PostConstruct
    public void initToken() {
        ResponseEntity<TokenResponse> entity = acquireToken();
        if (HttpStatus.OK == entity.getStatusCode() && entity.getBody() != null) {
            token = entity.getBody().getToken();
            log.info("获得Token：{}", token);
        } else {
            log.warn("获取Token失败，原因：{}", entity.getBody());
        }
    }

    @Override
    public void customize(ClientHttpRequest request) {
        if (StringUtils.isBlank(token)) {
            initToken();
        }
        if (StringUtils.isNotBlank(token) &&
                !request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            request.getHeaders().setBearerAuth(token);
        }
    }

    private ResponseEntity<TokenResponse> acquireToken() {
        return new RestTemplate(requestFactory) // 用个简单的RestTemplate来获取Token
                .postForEntity(binarytea + "/token",
                        new TokenRequest(username, password), TokenResponse.class);
    }
}
