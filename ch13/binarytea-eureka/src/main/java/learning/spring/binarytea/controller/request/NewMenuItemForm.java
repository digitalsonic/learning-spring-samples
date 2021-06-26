package learning.spring.binarytea.controller.request;

import learning.spring.binarytea.model.Size;
import lombok.Getter;
import lombok.Setter;
import org.joda.money.Money;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class NewMenuItemForm {
    @NotBlank
    private String name;
    @NotNull
    private Money price;
    @NotNull
    private Size size;
}
