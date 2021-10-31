package learning.spring.binarytea.integration;

import lombok.*;

@Getter
@Setter
@Builder
public class OrderMessage {
    private Long orderId;
    private Long teaMakerId;
    private String state;
}
