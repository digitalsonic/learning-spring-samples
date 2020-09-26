package learning.spring.binarytea.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private TeaMaker maker;
    private List<MenuItem> items;
    private Amount amount;
    private OrderStatus status;
    private Date createTime;
    private Date updateTime;
}
