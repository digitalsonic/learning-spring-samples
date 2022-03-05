package learning.spring.binarytea.repository;

import learning.spring.binarytea.model.Order;
import learning.spring.binarytea.model.TeaMaker;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {"data.init.enable=true",
        "spring.sql.init.mode=never", "spring.jpa.hibernate.ddl-auto=create-drop"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TeaMakerRepositoryTest {
    @Autowired
    private TeaMakerRepository makerRepository;

    @Test
    @Transactional
    public void testFindById() {
        TeaMaker maker = makerRepository.getById(4L);
        assertNotNull(maker);
        assertEquals("LiLei", maker.getName());

        List<Order> list = maker.getOrders();
        assertNotNull(list);
        assertEquals(2, list.size());
    }
}
