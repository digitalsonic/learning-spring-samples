package learning.spring;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DatasourceDemoApplicationTests {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void testDataSource() throws SQLException {
        assertTrue(applicationContext.containsBean("dataSource"));
        DataSource dataSource = applicationContext
                .getBean("dataSource", DataSource.class);
        assertTrue(dataSource instanceof DruidDataSource);

        Connection connection = dataSource.getConnection();
        assertTrue(connection instanceof DruidPooledConnection);
        connection.close();

        assertEquals(DruidDataSource.DEFAULT_MAX_ACTIVE_SIZE,
                ((DruidDataSource) dataSource).getMaxActive());
    }
}
