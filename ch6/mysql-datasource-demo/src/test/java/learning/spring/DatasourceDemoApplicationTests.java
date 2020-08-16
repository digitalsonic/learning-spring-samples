package learning.spring;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariProxyConnection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        assertTrue(dataSource instanceof HikariDataSource);

        HikariDataSource hikari = (HikariDataSource) dataSource;
        assertEquals(20, hikari.getMaximumPoolSize());
        assertEquals(10, hikari.getMinimumIdle());
        assertEquals("com.mysql.cj.jdbc.Driver", hikari.getDriverClassName());
        assertEquals(jdbcUrl, hikari.getJdbcUrl());

        Connection connection = hikari.getConnection();
        assertNotNull(connection);
        connection.close();
    }
}

