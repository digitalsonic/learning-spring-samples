package learning.spring.binarytea.integration;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @SentinelResource(value = "tea-maker", blockHandler = "notFinished")
    public TeaMakerResult makeTea(Long id) {
        ResponseEntity<TeaMakerResult> entity =
                restTemplate.postForEntity(teaMakerUrl + "/order/{id}", null,
                        TeaMakerResult.class, id);
        log.info("请求TeaMaker，响应码：{}", entity.getStatusCode());
        if (entity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return null;
        }
        return entity.getBody();
    }

    public TeaMakerResult notFinished(Long id, BlockException e) {
        log.warn("Blocked by Sentinel - {}", e.getMessage());
        TeaMakerResult result = new TeaMakerResult();
        result.setFinish(false);
        result.setOrderId(id);
        return result;
    }
}
