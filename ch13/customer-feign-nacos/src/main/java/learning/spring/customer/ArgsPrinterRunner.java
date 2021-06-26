package learning.spring.customer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
@Order(1)
public class ArgsPrinterRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        log.info("共传入了{}个参数。分别是：{}", args.length,
                StringUtils.arrayToCommaDelimitedString(args));
    }
}
