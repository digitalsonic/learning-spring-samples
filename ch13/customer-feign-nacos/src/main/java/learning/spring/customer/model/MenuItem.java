package learning.spring.customer.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.money.Money;

import java.util.Date;

@Getter
@Setter
@ToString
public class MenuItem {
    private Long id;
    private String name;
    private String size;
    private Money price;
    private Date createTime;
    private Date updateTime;
}
