package learning.spring.binarytea.dubbo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class MenuItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String size;
    private Long price;
    private Date createTime;
    private Date updateTime;
}
