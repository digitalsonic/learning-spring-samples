package learning.spring.binarytea;

import com.fasterxml.jackson.databind.ObjectMapper;
import learning.spring.binarytea.model.MenuItem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@SpringBootApplication
public class BinaryTeaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BinaryTeaApplication.class, args);
    }

    @Bean
    public RedisTemplate<String, MenuItem> redisTemplate(RedisConnectionFactory connectionFactory,
                                                         ObjectMapper objectMapper) {
        Jackson2JsonRedisSerializer<MenuItem> serializer = new Jackson2JsonRedisSerializer<>(MenuItem.class);
        serializer.setObjectMapper(objectMapper);

        RedisTemplate<String, MenuItem> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(serializer);
        return redisTemplate;
    }
}