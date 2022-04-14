package learning.spring.binarytea.repository;

import learning.spring.binarytea.model.Order;
import learning.spring.binarytea.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatusOrderById(OrderStatus status);
    List<Order> findByMaker_NameLikeIgnoreCaseOrderByUpdateTimeDescId(String name);
    List<Order> findByStatusEqualsAndIdInOrderById(OrderStatus status, List<Long> idList);
}
