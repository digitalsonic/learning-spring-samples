package learning.spring.binarytea.repository;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import learning.spring.binarytea.model.MenuItem;
import learning.spring.binarytea.model.Size;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MenuItemMapperTest {
    @Autowired
    private MenuItemMapper menuItemMapper;

    @Test
    public void testSelect() {
        assertEquals(2, menuItemMapper.selectCount(null));

        MenuItem item = menuItemMapper.selectById(1L);
        assertEquals(1L, item.getId());
        assertEquals("Java咖啡", item.getName());
        assertEquals(Size.MEDIUM, item.getSize());
        assertEquals(Money.ofMinor(CurrencyUnit.of("CNY"), 1200), item.getPrice());

        List<MenuItem> list = menuItemMapper.selectList(null);
        assertEquals(2, list.size());
    }

    @Test
    public void testPagination() {
        Page<MenuItem> page = menuItemMapper.selectPage(
                new Page<MenuItem>(1, 1).addOrder(OrderItem.asc("id")), null);
        assertEquals(1, page.getCurrent());
        assertEquals(1L, page.getRecords().get(0).getId());
        assertEquals(1, page.getRecords().size());
        assertEquals(2, page.getTotal());
    }
}
