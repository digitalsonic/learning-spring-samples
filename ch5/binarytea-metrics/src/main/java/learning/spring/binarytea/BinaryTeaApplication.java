package learning.spring.binarytea;

import learning.spring.binarytea.actuator.SalesMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
        SpringApplication.run(BinaryTeaApplication.class, args);
    }

    @Scheduled(fixedRate = 5000, initialDelay = 1000)
    public void periodicallyMakeAnOrder() {
        int amount = random.nextInt(100);
        salesMetrics.makeNewOrder(amount);
        logger.info("Make an order of RMB {} yuan.", amount);
    }
}
