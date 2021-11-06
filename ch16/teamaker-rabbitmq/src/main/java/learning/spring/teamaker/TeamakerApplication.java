package learning.spring.teamaker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TeamakerApplication {

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper jsonObjectMapper) {
        return new Jackson2JsonMessageConverter(jsonObjectMapper);
    }

    public static void main(String[] args) {
        SpringApplication.run(TeamakerApplication.class, args);
    }
}
