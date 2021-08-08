package learning.spring.binarytea.controller.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class StatusForm {
    @NotNull
    private Long id;
    @NotBlank
    private String status;
}
