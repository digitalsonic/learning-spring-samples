package learning.spring.binarytea.repository;

import learning.spring.binarytea.model.MenuItem;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
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

    @Test
    @Order(0)
    void testCountMenuItems() {
        assertEquals(2, menuRepository.countMenuItems());
    }

    @Test
    @Order(0)
    void testQueryAllItems() {
        List<MenuItem> items = menuRepository.queryAllItems();
        assertNotNull(items);
        assertFalse(items.isEmpty());
        assertEquals(2, items.size());
    }

    @Test
    @Order(0)
    void testQueryForItem() {
        MenuItem item = menuRepository.queryForItem(1L);
        assertItem(item, 1L, "Java咖啡", "中杯", BigDecimal.valueOf(10.00));
    }

    @Test
    @Order(1)
    void testInsertItem() {
        MenuItem item = MenuItem.builder()
                .name("Go橙汁").size("中杯")
                .price(BigDecimal.valueOf(12.00))
                .build();

        assertEquals(1, menuRepository.insertItem(item));
        assertNull(item.getId());
        MenuItem queryItem = menuRepository.queryForItem(3L);
        assertItem(queryItem, 3L, "Go橙汁", "中杯", BigDecimal.valueOf(12.00));

        assertEquals(1, menuRepository.insertItemAndFillId(item));
        queryItem = menuRepository.queryForItem(item.getId());
        assertItem(queryItem, 4L, "Go橙汁", "中杯", BigDecimal.valueOf(12.00));
    }

    @Test
    @Order(2)
    void testDelete() {
        assertEquals(1, menuRepository.deleteItem(3L));
        assertEquals(1, menuRepository.deleteItem(2L));
    }

    @Test
    @Order(3)
    void testInsertItems() {
        List<MenuItem> items = Stream.of("Go橙汁", "Python气泡水", "JavaScript苏打水")
                .map(n -> MenuItem.builder().name(n).size("中杯").price(BigDecimal.valueOf(12.00)).build())
                .collect(Collectors.toList());
        assertEquals(3, menuRepository.insertItems(items));
        assertItem(menuRepository.queryForItem(5L),
                5L, "Go橙汁", "中杯", BigDecimal.valueOf(12.00));
        assertItem(menuRepository.queryForItem(6L),
                6L, "Python气泡水", "中杯", BigDecimal.valueOf(12.00));
        assertItem(menuRepository.queryForItem(7L),
                7L, "JavaScript苏打水", "中杯", BigDecimal.valueOf(12.00));
    }

    private void assertItem(MenuItem item, Long id, String name, String size, BigDecimal price) {
        assertNotNull(item);
        assertEquals(id, item.getId());
        assertEquals(name, item.getName());
        assertEquals(size, item.getSize());
        assertEquals(price, item.getPrice());
    }
}