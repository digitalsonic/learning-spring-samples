package learning.spring.customer;

import com.fasterxml.jackson.datatype.jodamoney.JodaMoneyModule;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@SpringBootApplication
public class CustomerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(CustomerApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(1)) // 连接超时
                .setReadTimeout(Duration.ofSeconds(5)) // 读取超时
                .build();
    }

    @Bean
    public JodaMoneyModule jodaMoneyModule() {
        return new JodaMoneyModule();
    }

    /**
     * 如果命令行里给了wait选项，返回0，否则返回1
     */
    @Bean
    public ExitCodeGenerator waitExitCodeGenerator(ApplicationArguments args) {
        return () -> (args.containsOption("wait") ? 0 : 1);
    }
}
