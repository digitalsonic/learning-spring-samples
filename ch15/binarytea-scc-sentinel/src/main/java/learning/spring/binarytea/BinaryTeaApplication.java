package learning.spring.binarytea;

import com.alibaba.cloud.circuitbreaker.sentinel.SentinelCircuitBreakerFactory;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jodamoney.JodaMoneyModule;
import learning.spring.binarytea.support.log.LogHandlerInterceptor;
import learning.spring.binarytea.support.ready.ReadyStateCheckHandlerInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class BinaryTeaApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(BinaryTeaApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logHandlerInterceptor())
                .addPathPatterns("/**").excludePathPatterns("/static/**");
        registry.addInterceptor(readyStateCheckHandlerInterceptor())
                .addPathPatterns("/menu", "/menu/**", "/order", "/order/**");
    }

    @Bean
    public Customizer<SentinelCircuitBreakerFactory> teaMakerCustomizer() {
        DegradeRule rule = new DegradeRule();
        rule.setResource("tea-maker");
        rule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT);
        rule.setCount(3);
        rule.setTimeWindow(60);
        rule.setMinRequestAmount(3);
        rule.setStatIntervalMs(60000);

        return f -> f.configure(b -> b.resourceName("tea-maker")
                .rules(Collections.singletonList(rule)).entryType(EntryType.OUT), "tea-maker");
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ReadyStateCheckHandlerInterceptor readyStateCheckHandlerInterceptor() {
        return new ReadyStateCheckHandlerInterceptor();
    }

    @Bean
    public LogHandlerInterceptor logHandlerInterceptor() {
        return new LogHandlerInterceptor();
    }

    @Bean
    public JodaMoneyModule jodaMoneyModule() {
        return new JodaMoneyModule();
    }

    @Bean
    public Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }

    @Bean
    public ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }
}
