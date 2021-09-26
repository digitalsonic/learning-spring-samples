package learning.spring.customer.support;

import learning.spring.customer.integration.OrderService;
import learning.spring.customer.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.util.AopTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.*;

@SpringJUnitConfig(RetryAspectTest.RetryTestConfig.class)
class RetryAspectTest {
    @TestConfiguration
    @EnableAspectJAutoProxy
    static class RetryTestConfig {
        @Bean
        public RetryAspect retryAspect() {
            return new RetryAspect();
        }

        @Bean
        public OrderService orderService() {
            return mock(OrderService.class);
        }
    }

    @Autowired
    private OrderService orderService;
    private OrderService mockService;

    @BeforeEach
    public void setUp() {
        mockService = AopTestUtils.getUltimateTargetObject(orderService);
        reset(mockService);
    }

    @Test
    void testSuccessInvoke() {
        given(mockService.listOrders()).willReturn(Collections.emptyList());
        List<Order> orders = orderService.listOrders();
        then(mockService).should(only()).listOrders();
        assertTrue(orders.isEmpty());
    }

    @Test
    void testDoWithRetry() {
        given(mockService.listOrders()).willThrow(new RuntimeException())
                .willThrow(new RuntimeException()).willReturn(Collections.emptyList());
        List<Order> orders = orderService.listOrders();
        then(mockService).should(times(3)).listOrders();
        assertTrue(orders.isEmpty());
    }
}