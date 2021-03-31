package learning.spring.customer.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Order {
    private Long id;
    private List<MenuItem> items;
    private String status;
    private Date createTime;
    private Date updateTime;
}
