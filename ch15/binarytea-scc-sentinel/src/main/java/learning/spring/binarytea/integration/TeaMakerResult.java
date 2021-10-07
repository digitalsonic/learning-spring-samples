package learning.spring.binarytea.integration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeaMakerResult {
    private boolean finish;
    private long orderId;
    private long teaMakerId;
}
