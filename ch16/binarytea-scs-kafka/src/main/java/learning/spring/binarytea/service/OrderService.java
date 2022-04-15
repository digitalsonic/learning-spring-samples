package learning.spring.binarytea.service;

import learning.spring.binarytea.BinaryTeaProperties;
import learning.spring.binarytea.integration.OrderFinishedEvent;
import learning.spring.binarytea.integration.OrderMessage;
import learning.spring.binarytea.integration.TeaMakerClient;
import learning.spring.binarytea.model.Amount;
import learning.spring.binarytea.model.MenuItem;
import learning.spring.binarytea.model.Order;
import learning.spring.binarytea.model.OrderStatus;
import learning.spring.binarytea.repository.OrderRepository;
import learning.spring.binarytea.repository.TeaMakerRepository;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
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
    @Autowired
    private TeaMakerRepository teaMakerRepository;
    @Autowired
    private TeaMakerClient teaMakerClient;
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
        if (OrderStatus.PAID == status) {
            teaMakerClient.notifyPaidOrder(id);
            order.get().setStatus(OrderStatus.MAKING);
        }
        return orderRepository.save(order.get());
    }

    public Optional<Order> queryOrder(Long id) {
        return orderRepository.findById(id);
    }

    @RolesAllowed({ "MANAGER", "TEA_MAKER" })
    public int modifyOrdersState(List<Long> idList, OrderStatus oldState, OrderStatus newState) {
        List<Order> orders = orderRepository.findByStatusEqualsAndIdInOrderById(oldState, idList);
        orders.forEach(o -> o.setStatus(newState));
        return orderRepository.saveAll(orders).size();
    }

    @EventListener
    public void finishOrder(OrderFinishedEvent event) {
        OrderMessage message = (OrderMessage) event.getSource();
        // 没考虑性能等问题
        Order order = modifyOrderStatus(message.getOrderId(), OrderStatus.FINISHED);
        if (order != null) {
            teaMakerRepository.findById(message.getTeaMakerId()).ifPresent(order::setMaker);
            orderRepository.save(order);
        }
    }
}
