package learning.spring.binarytea;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import learning.spring.binarytea.actuator.SalesMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Random;

@SpringBootApplication
@EnableScheduling
public class BinaryTeaApplication {
    private static Logger logger = LoggerFactory.getLogger(BinaryTeaApplication.class);
    private Random random = new Random();
    @Autowired
    private SalesMetrics salesMetrics;

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(BinaryTeaApplication.class)
                .main(BinaryTeaApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

    @Bean
    public MeterRegistry customMeterRegistry() {
        CompositeMeterRegistry meterRegistry = new CompositeMeterRegistry();
        meterRegistry.add(new SimpleMeterRegistry());
        meterRegistry.add(new LoggingMeterRegistry());
        return meterRegistry;
    }

    @Scheduled(fixedRate = 5000, initialDelay = 1000)
    public void periodicallyMakeAnOrder() {
        int amount = random.nextInt(100);
        salesMetrics.makeNewOrder(amount);
        logger.info("Make an order of RMB {} yuan.", amount);
    }
}
