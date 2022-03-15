package learning.spring.binarytea.repository;

import learning.spring.binarytea.model.MenuItem;
import learning.spring.binarytea.model.Size;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MenuRepositoryTest {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @AfterEach
    public void tearDown() {
        jdbcTemplate = null;
    }

    @Test
    @Order(1)
    void testInsertItem() {
        List<MenuItem> items = Stream.of("Go橙汁", "Python气泡水", "JavaScript苏打水")
                .map(n -> MenuItem.builder().name(n)
                        .size(Size.MEDIUM)
                        .price(Money.ofMinor(CurrencyUnit.of("CNY"), 1200))
                        .build())
                .peek(m -> menuRepository.insertItem(m))
                .collect(Collectors.toList());

        for (int i = 0; i < 3; i++) {
            assertEquals(i + 1, items.get(i).getId());
            assertItem(i + 1L, items.get(i).getName());
        }
    }

    @Test
    @Order(2)
    void testCountMenuItems() {
        assertEquals(3, menuRepository.countMenuItems());
    }

    @Test
    @Order(3)
    void testQueryForItem() {
        MenuItem item = menuRepository.queryForItem(1L);
        assertNotNull(item);
        assertItem(1L, "Go橙汁");
    }

    @Test
    @Order(4)
    void testQueryAllItems() {
        List<MenuItem> items = menuRepository.queryAllItems();
        assertNotNull(items);
        assertFalse(items.isEmpty());
        assertEquals(3, items.size());
    }

    @Test
    @Order(5)
    void testUpdateItem() {
        MenuItem item = menuRepository.queryForItem(1L);
        item.setPrice(Money.ofMinor(CurrencyUnit.of("CNY"), 1100));
        menuRepository.updateItem(item);
        Long price = jdbcTemplate.queryForObject("select price from t_menu where id = 1", Long.class);
        assertEquals(1100L, price);
    }

    @Test
    @Order(6)
    void testDeleteItem() {
        menuRepository.deleteItem(2L);
        assertNull(menuRepository.queryForItem(2L));
    }

    private void assertItem(Long id, String name) {
        Map<String, Object> result = jdbcTemplate.queryForMap("select * from t_menu where id = ?", id);
        assertEquals(name, result.get("name"));
        assertEquals(Size.MEDIUM.name(), result.get("size"));
        assertEquals(1200L, result.get("price"));
    }
}