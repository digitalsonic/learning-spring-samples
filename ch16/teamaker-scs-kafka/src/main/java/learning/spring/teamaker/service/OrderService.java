package learning.spring.teamaker.service;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import learning.spring.teamaker.model.ProcessResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class OrderService {
    @Value("${tea-maker.id:-1}")
    private long teaMakerId;
    @Value("${tea-maker.time-per-order:500ms}")
    private Duration timePerOrder;
//    @Autowired
//    private RateLimiterRegistry rateLimiterRegistry;
//
//    public ProcessResult make(Long id) {
//        RateLimiter rateLimiter = rateLimiterRegistry.rateLimiter("make-tea");
//        return rateLimiter.executeSupplier(() -> {
//            try {
//                TimeUnit.MILLISECONDS.sleep(timePerOrder.toMillis());
//            } catch (InterruptedException e) {
//            }
//            return ProcessResult.builder()
//                    .finish(true).orderId(id).teaMakerId(teaMakerId)
//                    .build();
//        });
//    }

    @RateLimiter(name = "make-tea")
    public ProcessResult make(Long id) {
        try {
            TimeUnit.MILLISECONDS.sleep(timePerOrder.toMillis());
        } catch (InterruptedException e) {
        }
        return ProcessResult.builder()
                .finish(true).orderId(id).teaMakerId(teaMakerId)
                .build();
    }
}
