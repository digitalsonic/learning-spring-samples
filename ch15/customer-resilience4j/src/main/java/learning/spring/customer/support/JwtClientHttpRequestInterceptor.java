package learning.spring.customer.support;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import learning.spring.customer.model.TokenRequest;
import learning.spring.customer.model.TokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class JwtClientHttpRequestInterceptor implements RequestInterceptor {
    @Value("${jwt.username}")
    private String username;
    @Value("${jwt.password}")
    private String password;
    @Value("${binarytea.url}")
    private String binarytea;
    private String token;
    @Autowired
    @Lazy
    private RestTemplate restTemplate;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        if (StringUtils.isBlank(token)) {
            initToken();
        }
        requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }

    public void initToken() {
        ResponseEntity<TokenResponse> entity = acquireToken();
        if (HttpStatus.OK == entity.getStatusCode() && entity.getBody() != null) {
            token = entity.getBody().getToken();
            log.info("获得Token：{}", token);
        } else {
            log.warn("获取Token失败，原因：{}", entity.getBody());
        }
    }

    private ResponseEntity<TokenResponse> acquireToken() {
        return restTemplate // 直接用RestTemplate来获取Token，这里要跳过令牌校验
                .postForEntity(binarytea + "/token",
                        new TokenRequest(username, password), TokenResponse.class);
    }
}
