package learning.spring.binarytea.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
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
