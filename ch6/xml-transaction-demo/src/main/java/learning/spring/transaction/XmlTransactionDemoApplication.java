package learning.spring.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@Slf4j
@ImportResource("applicationContext.xml")
public class XmlTransactionDemoApplication implements ApplicationRunner {
	@Autowired
	private MixService mixService;
	@Autowired
	private DemoService demoService;

	public static void main(String[] args) {
		SpringApplication.run(XmlTransactionDemoApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		try {
			mixService.trySomeMethods();
		} catch (Exception e) {}
		log.info("Names: {}", demoService.showNames());
	}
}
