package learning.spring.binarytea.repository;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import learning.spring.binarytea.model.MenuItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MenuItemMapperTest {
    @Autowired
    private MenuItemMapper menuItemMapper;

    @Test
    public void testPagination() {
        // 不分页
        List<MenuItem> list = menuItemMapper.findAll();
        assertEquals(2, list.size());

        // 分页
        PageHelper.startPage(1, 1);
        list = menuItemMapper.findAll();
        assertEquals(1, list.size());
        assertTrue(list instanceof Page);
        PageInfo<MenuItem> pageInfo = new PageInfo<>(list);
        assertEquals(2, pageInfo.getPages()); // 总页数
        assertEquals(1, pageInfo.getPageSize()); // 每页大小
        assertEquals(1, pageInfo.getPageNum()); // 当前页码
        assertEquals(2, pageInfo.getNextPage()); // 下页页码
    }
}
