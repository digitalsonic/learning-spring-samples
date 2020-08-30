package learning.spring.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class DemoService {
    public static final String SQL =
            "insert into t_demo (name, create_time, update_time) values (?, now(), now())";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String showNames() {
        return jdbcTemplate.queryForList("select name from t_demo;", String.class)
                .stream().collect(Collectors.joining(","));
    }

    public void insertRecordRequired() {
        jdbcTemplate.update(SQL, "one");
    }

    public void insertRecordRequiresNew() {
        jdbcTemplate.update(SQL, "two");
    }

    public void insertRecordNested() {
        jdbcTemplate.update(SQL, "three");
        throw new RuntimeException(); // 让事务回滚
    }
}
