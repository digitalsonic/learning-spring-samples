package learning.spring.customer;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
@Slf4j
@Order(2)
public class WaitForOpenRunner implements ApplicationRunner, ApplicationContextAware {
    @Setter
    private ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        boolean needWait = args.containsOption("wait");
        if (!needWait) {
            log.info("如果没开门，就不用等了。");
        } else {
            List<String> waitSeconds = args.getOptionValues("wait");
            if (!waitSeconds.isEmpty()) {
                int seconds = NumberUtils.parseNumber(waitSeconds.get(0),
                        Integer.class);
                log.info("还没开门，先等{}秒。", seconds);
                Thread.sleep(seconds * 1000);
            }

            log.info("其他参数：{}",
                    StringUtils.collectionToCommaDelimitedString(
                            args.getNonOptionArgs()));
        }

        System.exit(SpringApplication.exit(applicationContext));
    }
}
