package learning.spring.customer;

import com.fasterxml.jackson.datatype.jodamoney.JodaMoneyModule;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@Slf4j
public class CustomerApplication {
    @Value("${binarytea.ssl.key-store}")
    private Resource keyStore;
    @Value("${binarytea.ssl.key-store-password}")
    private String keyStorePassword;

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(CustomerApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .requestFactory(this::requestFactory)
                .setConnectTimeout(Duration.ofSeconds(1)) // 连接超时
                .setReadTimeout(Duration.ofSeconds(5)) // 读取超时
                .additionalRequestCustomizers(jwtClientHttpRequestInitializer())
                .build();
    }

    @Bean
    public JwtClientHttpRequestInitializer jwtClientHttpRequestInitializer() {
        return new JwtClientHttpRequestInitializer();
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

    @Bean
    public ClientHttpRequestFactory requestFactory() {
        // 构造SSLSocketFactory，配置密钥等信息
        SSLSocketFactory sslSocketFactory = null;
        TrustManagerFactory tmf = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(this.keyStore.getInputStream(), keyStorePassword.toCharArray());
            tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            log.error("Can NOT create sslSocketFactory and ", e);
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1))  // 默认就这样
                .connectTimeout(10, TimeUnit.SECONDS) // 以下3个超时默认就是10秒
                .callTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(50, 30, TimeUnit.MINUTES));

        if (sslSocketFactory != null) {
            builder.connectionSpecs(Collections.singletonList(ConnectionSpec.RESTRICTED_TLS))
                    .hostnameVerifier((hostname, session) -> true) // 不验证主机
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) tmf.getTrustManagers()[0]);
        }

        return new OkHttp3ClientHttpRequestFactory(builder.build());
    }
}
