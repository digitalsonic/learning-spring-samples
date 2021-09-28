package learning.spring.customer.support;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

import java.util.concurrent.TimeUnit;

@Aspect
@Slf4j
@Order(0)
public class RetryAspect {
    private static final int THRESHOLD = 3;
    private static final int DURATION = 100;

    @Around("execution(* learning.spring.customer.integration..*(..))")
    public Object doWithRetry(ProceedingJoinPoint pjp) throws Throwable {
        String signature = pjp.getSignature().toLongString();
        log.info("带重试机制调用{}方法", signature);
        Object ret = null;
        Exception lastEx = null;
        for (int i = 1; i <= THRESHOLD; i++) {
            try {
                ret = pjp.proceed();
                log.info("在第{}次完成了{}调用", i, signature);
                return ret;
            } catch (Exception e) {
                log.warn("执行失败", e);
                lastEx = e;
                try {
                    TimeUnit.MILLISECONDS.sleep(DURATION);
                } catch (InterruptedException ie) {
                }
            }
        }
        log.error("{}方法最终执行失败，抛出异常{}", signature, lastEx);
        throw lastEx;
    }
}
