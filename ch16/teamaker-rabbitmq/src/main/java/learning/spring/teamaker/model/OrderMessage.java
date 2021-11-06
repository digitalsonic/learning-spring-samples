package learning.spring.teamaker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderMessage {
    private Long orderId;
    private Long teaMakerId;
    private String state;
}
