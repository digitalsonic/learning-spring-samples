package learning.spring.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Amount {
    private int discount;
    private Money totalAmount;
    private Money payAmount;
}
