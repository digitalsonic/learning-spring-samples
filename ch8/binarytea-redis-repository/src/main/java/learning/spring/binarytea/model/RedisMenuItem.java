package learning.spring.binarytea.model;

import lombok.Getter;
import lombok.Setter;
import org.joda.money.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@RedisHash(value = "menu", timeToLive = 60)
@Getter
@Setter
public class RedisMenuItem implements Serializable {
    private static final long serialVersionUID = 4442333144469925590L;

    @Id
    private Long id;
    @Indexed
    private String name;
    private Size size;
    private Money price;
}
