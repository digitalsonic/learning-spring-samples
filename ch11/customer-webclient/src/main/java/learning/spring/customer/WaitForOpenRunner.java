package learning.spring.customer;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@Slf4j
@Order(2)
public class WaitForOpenRunner implements ApplicationRunner, ApplicationContextAware {
    @Setter
    private ApplicationContext applicationContext;
    @Autowired
    private WebClient webClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        boolean flag = isOpen(); // 开门了么？
        flag = flag ? true : (waitForOpen(args) && isOpen()); // 没开门，想等一下再看看
        if (!flag) { // 没开门就退出
            log.info("店没开门，走了");
            System.exit(SpringApplication.exit(applicationContext));
        } else {
            log.info("店开门了，进去看看");
        }
    }

    private boolean waitForOpen(ApplicationArguments args) throws InterruptedException {
        boolean needWait = args.containsOption("wait");
        if (!needWait) {
            log.info("如果没开门，就不用等了。");
        } else {
            List<String> waitSeconds = args.getOptionValues("wait");
            if (!waitSeconds.isEmpty()) {
                int seconds = NumberUtils.parseNumber(waitSeconds.get(0), Integer.class);
                log.info("还没开门，先等{}秒。", seconds);
                Thread.sleep(seconds * 1000);
            }
        }
        return needWait;
    }

    private boolean isOpen() {
        try {
            ResponseEntity<Void> entity = webClient.get().uri("/menu")
                    .retrieve().toBodilessEntity().block();
            return entity.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.warn("应该还没开门，访问出错：{}", e);
        }
        return false;
    }
}
