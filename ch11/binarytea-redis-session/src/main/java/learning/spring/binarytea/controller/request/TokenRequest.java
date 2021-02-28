package learning.spring.binarytea.controller.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class TokenRequest {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
