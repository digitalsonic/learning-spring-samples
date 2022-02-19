package learning.spring.binarytea.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class TeaMaker {
    private Long id;
    private String name;
    private List<Order> orders;
    private Date createTime;
    private Date updateTime;
}
