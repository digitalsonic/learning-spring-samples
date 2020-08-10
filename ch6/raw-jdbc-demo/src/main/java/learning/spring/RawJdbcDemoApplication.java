package learning.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringBootApplication
@Slf4j
public class RawJdbcDemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(RawJdbcDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Class.forName("org.h2.Driver");
        // 此处使用了try-with-resource的语法，因此不用在finally段中关闭资源
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:test_db");
             Statement statement = connection.createStatement();
			 ResultSet resultSet =
                     statement.executeQuery("SELECT X FROM SYSTEM_RANGE(1, 10)")) {
            while (resultSet.next()) {
            	log.info("取值：{}", resultSet.getInt(1));
			}
        } catch (Exception e) {
            log.error("出错啦", e);
        }
    }
}
