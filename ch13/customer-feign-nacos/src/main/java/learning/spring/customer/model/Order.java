package learning.spring.customer.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class Order {
    private Long id;
    private TeaMaker maker;
    private Amount amount;
    private List<MenuItem> items;
    private String status;
    private Date createTime;
    private Date updateTime;
}
