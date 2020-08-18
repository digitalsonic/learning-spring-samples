package learning.spring.binarytea.repository;

import learning.spring.binarytea.model.MenuItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MenuRepositoryTest {
    @Autowired
    private MenuRepository menuRepository;

    @Test
    void testCountMenuItems() {
        assertEquals(2, menuRepository.countMenuItems());
    }

    @Test
    void testQueryAllItems() {
        List<MenuItem> items = menuRepository.queryAllItems();
        assertNotNull(items);
        assertFalse(items.isEmpty());
        assertEquals(2, items.size());
    }

    @Test
    void testQueryForItem() {
        MenuItem item = menuRepository.queryForItem(1L);
        assertNotNull(item);
        assertEquals(1L, item.getId());
        assertEquals("Java咖啡", item.getName());
        assertEquals("中杯", item.getSize());
        assertEquals(BigDecimal.valueOf(10.00), item.getPrice());
    }
}