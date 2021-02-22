package learning.spring.binarytea;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jodamoney.JodaMoneyModule;
import learning.spring.binarytea.support.log.LogHandlerInterceptor;
import learning.spring.binarytea.support.ready.ReadyStateCheckHandlerInterceptor;
import org.apache.coyote.http2.Http2Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableCaching
public class BinaryTeaApplication implements WebMvcConfigurer,
        WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    public static void main(String[] args) {
        SpringApplication.run(BinaryTeaApplication.class, args);
    }

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addConnectorCustomizers(connector -> connector.addUpgradeProtocol(new Http2Protocol()));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logHandlerInterceptor())
                .addPathPatterns("/**").excludePathPatterns("/static/**");
        registry.addInterceptor(readyStateCheckHandlerInterceptor())
                .addPathPatterns("/menu", "/menu/**", "/order", "/order/**");
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
