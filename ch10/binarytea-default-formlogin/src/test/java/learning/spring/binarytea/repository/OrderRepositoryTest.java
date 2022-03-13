package learning.spring.binarytea.repository;

import learning.spring.binarytea.model.Order;
import learning.spring.binarytea.model.OrderStatus;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {"data.init.enable=true",
        "spring.sql.init.mode=never", "spring.jpa.hibernate.ddl-auto=create-drop"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @Transactional
    public void testFindByStatusOrderById() {
        assertTrue(orderRepository.findByStatusOrderById(OrderStatus.FINISHED).isEmpty());
        List<Order> list = orderRepository.findByStatusOrderById(OrderStatus.ORDERED);
        assertEquals(2, list.size());
        assertEquals("Go橙汁", list.get(0).getItems().get(0).getName());
        assertEquals("Python气泡水", list.get(1).getItems().get(0).getName());
        assertTrue(list.get(0).getId() < list.get(1).getId());
        assertEquals("LiLei", list.get(0).getMaker().getName());
        assertEquals("LiLei", list.get(1).getMaker().getName());
    }

    @Test
    public void testFindByMaker_NameLikeOrderByUpdateTimeDescId() {
        assertTrue(orderRepository.findByMaker_NameLikeIgnoreCaseOrderByUpdateTimeDescId("%han%")
                .isEmpty());
        List<Order> list = orderRepository.findByMaker_NameLikeIgnoreCaseOrderByUpdateTimeDescId("%lei%");
        assertEquals(2, list.size());
        assertTrue(list.get(0).getUpdateTime().getTime() >= list.get(1).getUpdateTime().getTime());
        assertTrue(list.get(0).getId() > list.get(1).getId());
    }
}
