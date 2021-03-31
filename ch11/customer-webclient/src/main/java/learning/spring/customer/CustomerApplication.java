package learning.spring.customer;

import com.fasterxml.jackson.datatype.jodamoney.JodaMoneyModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.HttpComponentsClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@Slf4j
public class CustomerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(CustomerApplication.class)
//                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder,
                               @Value("${binarytea.url}") String binarytea) {
        return builder.baseUrl(binarytea)
                .filter(jwtExchangeFilterFunction(builder)).build();
    }

    @Bean
    public ClientHttpConnector clientHttpConnector() {
        HttpAsyncClientBuilder clientBuilder = HttpAsyncClients.custom();
        clientBuilder
                .disableAutomaticRetries()
                .evictIdleConnections(TimeValue.ofMinutes(10))
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectionRequestTimeout(1, TimeUnit.SECONDS)
                        .setConnectTimeout(1, TimeUnit.SECONDS).build())
                .setKeepAliveStrategy((response, context) ->
                        TimeValue.ofSeconds(Arrays.asList(response.getHeaders(HttpHeaders.KEEP_ALIVE))
                                .stream()
                                .filter(h -> StringUtils.equalsIgnoreCase(h.getName(), "timeout")
                                        && StringUtils.isNumeric(h.getValue()))
                                .findFirst()
                                .map(h -> NumberUtils.toLong(h.getValue(), 300L))
                                .orElse(300L)));

        CloseableHttpAsyncClient client = clientBuilder.build();
        return new HttpComponentsClientHttpConnector(client);
    }

    @Bean
    public JwtExchangeFilterFunction jwtExchangeFilterFunction(WebClient.Builder builder) {
        return new JwtExchangeFilterFunction(builder);
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
