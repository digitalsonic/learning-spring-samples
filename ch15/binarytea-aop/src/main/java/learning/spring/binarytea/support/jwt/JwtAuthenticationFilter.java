package learning.spring.binarytea.support.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

public class JwtAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {
    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.startsWith(header, "Bearer ")) {
            return null;
        }
        String token = StringUtils.substring(header, 7);
        Jws<Claims> jws = jwtTokenHelper.parseToken(token);
        return jws.getBody().getSubject();
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "NO_PASSWORD_NEEDED";
    }
}
