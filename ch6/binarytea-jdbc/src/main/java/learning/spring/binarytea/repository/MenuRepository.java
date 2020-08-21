package learning.spring.binarytea.repository;

import learning.spring.binarytea.model.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;

@Repository
public class MenuRepository {
    public static final String INSERT_SQL =
            "insert into t_menu (name, size, price, create_time, update_time) values (?, ?, ?, now(), now())";
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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

    public int insertItem(MenuItem item) {
        String sql = "insert into t_menu (name, size, price, create_time, update_time) values " +
                "(:name, :size, :price, now(), now())";
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("name", item.getName());
        sqlParameterSource.addValue("size", item.getSize());
        sqlParameterSource.addValue("price",
                item.getPrice().multiply(BigDecimal.valueOf(100)).longValue());
        return namedParameterJdbcTemplate.update(sql, sqlParameterSource);
//        return jdbcTemplate.update(INSERT_SQL, item.getName(), item.getSize(),
//                item.getPrice().multiply(BigDecimal.valueOf(100)).longValue());
    }

    public int insertItemAndFillId(MenuItem item) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int affected = jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement =
                    con.prepareStatement(INSERT_SQL, PreparedStatement.RETURN_GENERATED_KEYS);
            // 也可以用 PreparedStatement preparedStatement =
            //                 con.prepareStatement(INSERT_SQL, new String[] { "id" });

            preparedStatement.setString(1, item.getName());
            preparedStatement.setString(2, item.getSize());
            preparedStatement.setLong(3,
                    item.getPrice().multiply(BigDecimal.valueOf(100)).longValue());
            return preparedStatement;
        }, keyHolder);
        if (affected == 1) {
            item.setId(keyHolder.getKey().longValue());
        }
        return affected;
    }

    public int deleteItem(Long id) {
        return jdbcTemplate.update("delete from t_menu where id = ?", id);
    }

    private RowMapper<MenuItem> rowMapper() {
        return (resultSet, rowNum) -> {
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
