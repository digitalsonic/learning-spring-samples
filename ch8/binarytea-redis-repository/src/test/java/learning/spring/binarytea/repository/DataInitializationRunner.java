package learning.spring.binarytea.repository;

import learning.spring.binarytea.model.Amount;
import learning.spring.binarytea.model.MenuItem;
import learning.spring.binarytea.model.Order;
import learning.spring.binarytea.model.OrderStatus;
import learning.spring.binarytea.model.Size;
import learning.spring.binarytea.model.TeaMaker;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataInitializationRunner implements ApplicationRunner {
    @Autowired
    private TeaMakerRepository makerRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
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
            Thread.sleep(200);
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
