package learning.spring.teamaker.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProcessResult {
    private boolean finish;
    private long orderId;
    private long teaMakerId;
}
