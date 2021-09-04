package learning.spring.customer.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StatusForm {
    private Long id;
    private String status;
}
