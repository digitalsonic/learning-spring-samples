package learning.spring.customer.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequest {
    private String username;
    private String password;
}
