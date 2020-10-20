package learning.spring.binarytea;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jodamoney.JodaMoneyModule;
import learning.spring.binarytea.support.BytesToMoneyConverter;
import learning.spring.binarytea.support.MoneyToBytesConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Arrays;

@SpringBootApplication
public class BinaryTeaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BinaryTeaApplication.class, args);
    }

    @Bean
    public JodaMoneyModule jodaMoneyModule() {
        return new JodaMoneyModule();
    }

    @Bean
    public RedisCustomConversions redisCustomConversions(ObjectMapper objectMapper) {
        return new RedisCustomConversions(
                Arrays.asList(new MoneyToBytesConverter(objectMapper), new BytesToMoneyConverter(objectMapper)));
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory connectionFactory,
                                       ObjectMapper objectMapper) {
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(serializer);
        return redisTemplate;
    }
}