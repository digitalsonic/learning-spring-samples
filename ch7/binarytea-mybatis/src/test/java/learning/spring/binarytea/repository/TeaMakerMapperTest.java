package learning.spring.binarytea.repository;

import com.github.pagehelper.PageInfo;
import learning.spring.binarytea.model.TeaMaker;
import org.apache.ibatis.session.RowBounds;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TeaMakerMapperTest {
    @Autowired
    private TeaMakerMapper teaMakerMapper;

    @Test
    public void testPagination() {
        List<TeaMaker> list = teaMakerMapper.findAllWithRowBounds(new RowBounds(1, 1));
        PageInfo<TeaMaker> pageInfo = new PageInfo<>(list);
        assertEquals(1, list.size());
        assertEquals(1, pageInfo.getPageNum());
        assertEquals(1, pageInfo.getPageSize());
        assertEquals(2, pageInfo.getPages());

        list = teaMakerMapper.findAllWithPage(1, 2);
        pageInfo = new PageInfo<>(list);
        assertEquals(2, pageInfo.getPageNum());
        assertEquals(1, pageInfo.getPrePage());
        assertEquals(0, pageInfo.getNextPage()); // 没有下一页
    }
}
