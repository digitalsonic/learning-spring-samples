package learning.spring.binarytea.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@Getter
@Setter
@ToString
public class MenuItem {
    private Long id;
    private String name;
    private String size;
    private BigDecimal price; // 暂时用BigDecimal表示金额
    private Date createTime;
    private Date updateTime;
}
