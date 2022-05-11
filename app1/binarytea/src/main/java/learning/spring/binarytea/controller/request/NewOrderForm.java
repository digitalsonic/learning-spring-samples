package learning.spring.binarytea.controller.request;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class NewOrderForm {
    @NotEmpty
    private List<String> itemIdList;
    @Min(50)
    @Max(100)
    private int discount;
}
