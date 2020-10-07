package learning.spring;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.sql.DataSource;

@SpringBootApplication
public class DatasourceDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatasourceDemoApplication.class, args);
    }

}
