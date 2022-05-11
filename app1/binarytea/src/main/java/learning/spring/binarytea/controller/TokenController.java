package learning.spring.binarytea.controller;

import learning.spring.binarytea.controller.request.TokenRequest;
import learning.spring.binarytea.controller.response.TokenResponse;
import learning.spring.binarytea.support.jwt.JwtTokenHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class TokenController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> createToken(@RequestBody @Valid TokenRequest tokenRequest,
                                                     BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage()).collect(Collectors.joining(";"));
            return ResponseEntity.badRequest().body(new TokenResponse(null, errorMessage));
        }
        try {
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            tokenRequest.getUsername(), tokenRequest.getPassword());
            authenticationManager.authenticate(authentication);
        } catch (AuthenticationException e) {
            log.warn("Login failed. User: {}, Reason: {}", tokenRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new TokenResponse(null, e.getMessage()));
        }

        return ResponseEntity.ok(new TokenResponse(generateToken(tokenRequest.getUsername()), null));
    }

    private String generateToken(String username) {
        String token = jwtTokenHelper.generateToken(username);
        log.info("为用户{}生成Token [{}]", username, token);
        return token;
    }
}
