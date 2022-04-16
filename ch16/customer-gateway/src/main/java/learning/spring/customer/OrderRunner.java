package learning.spring.customer;

import learning.spring.customer.integration.BinaryTeaClient;
import learning.spring.customer.integration.OrderService;
import learning.spring.customer.model.NewOrderForm;
import learning.spring.customer.model.StatusForm;
import learning.spring.customer.support.OrderNotFinishedException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Order(5)
@Setter
@Slf4j
public class OrderRunner implements ApplicationRunner {
    @Autowired
    private OrderService orderService;
    @Autowired
    private BinaryTeaClient client;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<learning.spring.customer.model.Order> orders = orderService.listOrders();
        log.info("调用前的订单数量: {}", orders.size());
        Long id = createOrder();
        modifyOrderState(id, "PAID");
        waitUntilFinished(id);
        modifyOrderState(id, "TAKEN");
    }

    private Long createOrder() {
        NewOrderForm form = NewOrderForm.builder()
                .itemIdList(Arrays.asList("1"))
                .discount(90).build();
        ResponseEntity<learning.spring.customer.model.Order> response =
                orderService.createNewOrder(form);
        log.info("HTTP Status: {}, Headers: {}", response.getStatusCode(), response.getHeaders());
        log.info("Body: {}", response.getBody());
        return response.getBody().getId();
    }

    private void modifyOrderState(Long id, String state) {
        log.info("订单{}改为{}状态", id, state);
        StatusForm sf = StatusForm.builder().id(id).status(state).build();
        ResponseEntity<learning.spring.customer.model.Order> response =
                orderService.modifyOrderStatus(sf);
        log.info("HTTP Status: {}, Headers: {}", response.getStatusCode(), response.getHeaders());
        log.info("Body: {}", response.getBody());
    }

    private void waitUntilFinished(Long id) {
        boolean flag = false;
        while (!flag) {
            try {
                flag = client.isOrderFinished(id);
                log.info("订单{}完成了没{}", id, flag);
                if (!flag) {
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (IllegalArgumentException e) {
                log.warn("没找到订单{}", id);
                break;
            } catch (OrderNotFinishedException e) {
                log.info("订单{}还没准备好", id);
            } catch (Exception e) {
                log.error("啊呀有问题", e);
            }
        }
        log.info("订单{}已经好了", id);
    }
}
