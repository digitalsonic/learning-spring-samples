package learning.spring.binarytea.service;

import learning.spring.binarytea.BinaryTeaProperties;
import learning.spring.binarytea.model.Amount;
import learning.spring.binarytea.model.MenuItem;
import learning.spring.binarytea.model.Order;
import learning.spring.binarytea.model.OrderStatus;
import learning.spring.binarytea.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired(required = false)
    private BinaryTeaProperties binaryTeaProperties;

    @Secured({ "ROLE_MANAGER", "ROLE_TEA_MAKER", "ROLE_USER" })
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @RolesAllowed({ "MANAGER", "TEA_MAKER" })
    public Order createOrder(List<MenuItem> itemList, int discount) {
        int newDiscount = discount == 100 ? binaryTeaProperties.getDiscount() : discount;
        Money total = itemList.stream().map(i -> i.getPrice())
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        l -> Money.total(l)));
        Money pay = total.multipliedBy(newDiscount / 100d, RoundingMode.HALF_DOWN);

        Amount amount = Amount.builder()
                .discount(newDiscount)
                .totalAmount(total)
                .payAmount(pay)
                .build();
        Order order = Order.builder()
                .amount(amount)
                .status(OrderStatus.ORDERED)
                .items(itemList)
                .build();
        return orderRepository.save(order);
    }

    public Order modifyOrderStatus(Long id, OrderStatus status) {
        Optional<Order> order = orderRepository.findById(id);
        if (!order.isPresent()) {
            log.warn("订单{}不存在", id);
            return null;
        }
        OrderStatus old = order.get().getStatus();
        if (status.getIndex() != old.getIndex() + 1) {
            log.warn("订单{}无法从状态{}改为{}", id, old, status);
            return null;
        }
        order.get().setStatus(status);
        return orderRepository.save(order.get());
    }

    @RolesAllowed({ "MANAGER", "TEA_MAKER" })
    public int modifyOrdersState(List<Long> idList, OrderStatus oldState, OrderStatus newState) {
        List<Order> orders = orderRepository.findByStatusEqualsAndIdInOrderById(oldState, idList);
        orders.forEach(o -> o.setStatus(newState));
        return orderRepository.saveAll(orders).size();
    }
}
