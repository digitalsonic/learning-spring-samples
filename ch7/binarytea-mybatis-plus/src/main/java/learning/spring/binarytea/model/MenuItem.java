package learning.spring.binarytea.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_menu")
public class MenuItem {
    private Long id;
    private String name;
    private Size size;
    private Money price;
    private Date createTime;
    private Date updateTime;
}

