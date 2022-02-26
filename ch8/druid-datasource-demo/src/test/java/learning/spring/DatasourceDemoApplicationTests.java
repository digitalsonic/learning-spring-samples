package learning.spring;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DatasourceDemoApplicationTests {
    @Autowired
    private ApplicationContext applicationContext;
    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Test
    void testDataSource() throws SQLException {
        assertTrue(applicationContext.containsBean("dataSource"));
        DataSource dataSource = applicationContext
                .getBean("dataSource", DataSource.class);
        assertTrue(dataSource instanceof DruidDataSource);

        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        connection.close();
    }
}

