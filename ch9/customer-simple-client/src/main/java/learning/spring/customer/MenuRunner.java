package learning.spring.customer;

import learning.spring.customer.model.MenuItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
@Slf4j
@Order(3)
public class MenuRunner implements ApplicationRunner {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${binarytea.url}")
    private String binarytea;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        MenuItem item = restTemplate.getForObject(binarytea + "/menu/{id}", MenuItem.class, 1);
        log.info("菜单上第一个是{}", item);

        String coffee = restTemplate.getForObject(binarytea + "/menu?name={name}", String.class,
                Collections.singletonMap("name", "Java咖啡"));
        log.info("有Java咖啡么？{}", coffee);

        String menuJson = restTemplate.getForObject(binarytea + "/menu", String.class);
        log.info("完整菜单：{}", menuJson);
    }
}
