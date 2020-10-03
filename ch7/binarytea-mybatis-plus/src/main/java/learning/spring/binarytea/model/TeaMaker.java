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
public class TeaMaker {
    private Long id;
    private String name;
    private List<Order> orders;
    private Date createTime;
    private Date updateTime;
}
