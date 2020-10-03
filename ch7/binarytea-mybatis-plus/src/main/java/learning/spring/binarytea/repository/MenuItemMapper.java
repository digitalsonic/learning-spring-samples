package learning.spring.binarytea.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import learning.spring.binarytea.model.MenuItem;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MenuItemMapper extends BaseMapper<MenuItem> {
    @Select("select m.* from t_menu m, t_order_item i where m.id = i.item_id and i.order_id = #{orderId}")
    List<MenuItem> findByOrderId(Long orderId);
}
