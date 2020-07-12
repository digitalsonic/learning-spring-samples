package learning.spring.helloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.info.SimpleInfoContributor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public SimpleInfoContributor simpleInfoContributor() {
		return new SimpleInfoContributor("simple", "HelloWorld!");
	}

	@RequestMapping("/helloworld")
	public String helloworld() {
		return "Hello World! Bravo Spring!";
	}
}
