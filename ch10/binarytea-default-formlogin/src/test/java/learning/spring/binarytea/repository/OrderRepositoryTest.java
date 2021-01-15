package learning.spring.binarytea.repository;

import learning.spring.binarytea.model.Order;
import learning.spring.binarytea.model.*;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TeaMakerRepository makerRepository;
    @Autowired
    private MenuRepository menuRepository;

    @BeforeEach
    public void setUp() {
        init();
    }

    @AfterEach
    public void tearDown() {
        orderRepository.deleteAll();
        menuRepository.deleteAll();
        makerRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testFindByStatusOrderById() {
        assertTrue(orderRepository.findByStatusOrderById(OrderStatus.FINISHED).isEmpty());
        List<Order> list = orderRepository.findByStatusOrderById(OrderStatus.ORDERED);
        assertEquals(2, list.size());
        assertEquals("Go橙汁", list.get(0).getItems().get(0).getName());
        assertEquals("Python气泡水", list.get(1).getItems().get(0).getName());
        assertTrue(list.get(0).getId() < list.get(1).getId());
    }

    @Test
    public void testFindByMaker_NameLikeOrderByUpdateTimeDescId() {
        assertTrue(orderRepository.findByMaker_NameLikeIgnoreCaseOrderByUpdateTimeDescId("%han%").isEmpty());
        List<Order> list = orderRepository.findByMaker_NameLikeIgnoreCaseOrderByUpdateTimeDescId("%lei%");
        assertEquals(2, list.size());
        assertTrue(list.get(0).getUpdateTime().getTime() >= list.get(1).getUpdateTime().getTime());
        assertTrue(list.get(0).getId() > list.get(1).getId());
    }

    private void init() {
        List<MenuItem> menuItemList = Arrays.asList("Go橙汁", "Python气泡水", "JavaScript苏打水").stream()
                .map(n -> MenuItem.builder().name(n)
                        .size(Size.MEDIUM)
                        .price(Money.ofMinor(CurrencyUnit.of("CNY"), 1200))
                        .build())
                .map(m -> {
                    menuRepository.save(m);
                    return m;
                }).collect(Collectors.toList());

        List<TeaMaker> makerList = Arrays.asList("LiLei", "HanMeimei").stream()
                .map(n -> TeaMaker.builder().name(n).build())
                .map(m -> {
                    makerRepository.save(m);
                    return m;
                }).collect(Collectors.toList());

        Order order = Order.builder()
                .maker(makerList.get(0))
                .amount(Amount.builder()
                        .discount(90)
                        .totalAmount(Money.ofMinor(CurrencyUnit.of("CNY"), 1200))
                        .payAmount(Money.ofMinor(CurrencyUnit.of("CNY"), 1080))
                        .build())
                .items(Arrays.asList(menuItemList.get(0)))
                .status(OrderStatus.ORDERED)
                .build();
        orderRepository.save(order);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
        }

        order = Order.builder()
                .maker(makerList.get(0))
                .amount(Amount.builder()
                        .discount(100)
                        .totalAmount(Money.ofMinor(CurrencyUnit.of("CNY"), 1200))
                        .payAmount(Money.ofMinor(CurrencyUnit.of("CNY"), 1200))
                        .build())
                .items(Arrays.asList(menuItemList.get(1)))
                .status(OrderStatus.ORDERED)
                .build();
        orderRepository.save(order);
    }
}
