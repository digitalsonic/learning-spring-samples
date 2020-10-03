package learning.spring.binarytea.repository;

import learning.spring.binarytea.model.Amount;
import learning.spring.binarytea.model.Order;
import learning.spring.binarytea.model.OrderStatus;
import learning.spring.binarytea.model.TeaMaker;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class OrderMapperTest {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private TeaMakerMapper makerRepository;
    @Autowired
    private MenuItemMapper menuItemMapper;

    @Test
    @Transactional
    @Rollback
    public void testSaveAndFind() {
        TeaMaker maker = makerRepository.findById(2L);
        Order order = Order.builder()
                .status(OrderStatus.ORDERED)
                .maker(maker)
                .amount(Amount.builder()
                        .discount(90)
                        .totalAmount(Money.ofMinor(CurrencyUnit.of("CNY"), 1200))
                        .payAmount(Money.ofMinor(CurrencyUnit.of("CNY"), 1080))
                        .build())
                .build();
        assertEquals(1, orderMapper.save(order));

        Long orderId = order.getId();
        assertNotNull(orderId);
        assertEquals(1, orderMapper.addOrderItem(orderId, menuItemMapper.selectById(2L)));

        order = orderMapper.findById(orderId);
        assertEquals(OrderStatus.ORDERED, order.getStatus());
        assertEquals(90, order.getAmount().getDiscount());
        assertEquals(maker.getId(), order.getMaker().getId());
        assertEquals(1, order.getItems().size());
        assertEquals(2L, order.getItems().get(0).getId());
    }

    @Test
    public void testFindByMakerId() {
        // 测试找不到的情况
        List<Order> orders = orderMapper.findByMakerId(0L);
        assertNotNull(orders);
        assertTrue(orders.isEmpty());

        // 测试能找到的情况
        orders = orderMapper.findByMakerId(1L);
        assertFalse(orders.isEmpty());
        assertEquals(1, orders.size());

        Order order = orders.get(0);
        Money price = Money.ofMinor(CurrencyUnit.of("CNY"), 1200);
        assertEquals(OrderStatus.ORDERED, order.getStatus());
        assertEquals(100, order.getAmount().getDiscount());
        assertEquals(price, order.getAmount().getTotalAmount());
        assertEquals(price, order.getAmount().getPayAmount());
        assertNotNull(order.getItems());
        assertEquals(1, order.getItems().size());
    }
}
