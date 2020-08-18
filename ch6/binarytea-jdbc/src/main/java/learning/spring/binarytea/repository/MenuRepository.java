package learning.spring.binarytea.repository;

import learning.spring.binarytea.model.MenuItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public class MenuRepository {
    private JdbcTemplate jdbcTemplate;

    public MenuRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long countMenuItems() {
        return jdbcTemplate
                .queryForObject("select count(*) from t_menu", Long.class);
    }

    public List<MenuItem> queryAllItems() {
        return jdbcTemplate.query("select * from t_menu", rowMapper());
    }

    public MenuItem queryForItem(Long id) {
        return jdbcTemplate.queryForObject("select * from t_menu where id = ?",
                rowMapper(), id);
    }

    private RowMapper<MenuItem> rowMapper() {
        return  (resultSet, rowNum) -> {
            return MenuItem.builder()
                    .id(resultSet.getLong("id"))
                    .name(resultSet.getString("name"))
                    .size(resultSet.getString("size"))
                    .price(BigDecimal.valueOf(resultSet.getLong("price") / 100.0d))
                    .createTime(new Date(resultSet.getDate("create_time").getTime()))
                    .updateTime(new Date(resultSet.getDate("update_time").getTime()))
                    .build();
        };
    }
}
