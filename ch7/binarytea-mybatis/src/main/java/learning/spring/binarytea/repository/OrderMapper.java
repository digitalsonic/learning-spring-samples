package learning.spring.binarytea.repository;

import learning.spring.binarytea.model.MenuItem;
import learning.spring.binarytea.model.Order;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;

import java.util.List;

@Mapper
public interface OrderMapper {
    @Insert("insert into t_order " +
            "(maker_id, status, amount_discount, amount_pay, amount_total, create_time, update_time) " +
            "values (#{maker.id}, #{status, typeHandler=org.apache.ibatis.type.EnumOrdinalTypeHandler}, " +
            "#{amount.discount}, #{amount.payAmount}, #{amount.totalAmount}, now(), now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(Order order);

    @Insert("insert into t_order_item (order_id, item_id) values (#{orderId}, #{item.id})")
    int addOrderItem(Long orderId, MenuItem item);

    @Select("select * from t_order where id = #{id}")
    @Results(id = "orderMap", value = {
            @Result(column = "status", property = "status", typeHandler = EnumOrdinalTypeHandler.class),
            @Result(column = "amount_discount", property = "amount.discount"),
            @Result(column = "amount_total", property = "amount.totalAmount"),
            @Result(column = "amount_pay", property = "amount.payAmount"),
            @Result(column = "maker_id", property = "maker",
                    one = @One(select = "learning.spring.binarytea.repository.TeaMakerMapper.findById")),
            @Result(column = "id", property = "items",
                    many = @Many(select = "learning.spring.binarytea.repository.MenuItemMapper.findByOrderId"))
    })
    Order findById(Long id);

    @Select("select * from t_order where maker_id = #{makerId}")
    @ResultMap("orderMap")
    List<Order> findByMakerId(Long makerId);
}
