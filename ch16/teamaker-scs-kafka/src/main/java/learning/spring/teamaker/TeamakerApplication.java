package learning.spring.teamaker;

import learning.spring.teamaker.integration.BinaryteaClient;
import learning.spring.teamaker.model.OrderMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
public class TeaMakerApplication {

	@Bean
	public Function<OrderMessage, OrderMessage> notifyOrderPaid(BinaryteaClient client) {
		return message -> client.processOrder(message);
	}

	public static void main(String[] args) {
		SpringApplication.run(TeaMakerApplication.class, args);
	}
}
