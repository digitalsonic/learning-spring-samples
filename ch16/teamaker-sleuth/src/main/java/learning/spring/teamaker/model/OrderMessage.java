package learning.spring.teamaker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class OrderMessage {
    private Long orderId;
    private Long teaMakerId;
    private String state;
}
