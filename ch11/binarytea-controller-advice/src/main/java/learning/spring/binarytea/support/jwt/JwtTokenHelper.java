package learning.spring.binarytea.support.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenHelper implements InitializingBean {
    private static final String ISSUER = "BinaryTea";

    private JwtParser jwtParser;
    private Key key;

    @Value("${jwt.secret}")
    public void setBase64Key(String base64) {
        // this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        // log.info("使用密钥：{}", Base64.getEncoder().encodeToString(key.getEncoded()));
        // 密钥类似：gR6cytlUlgMfVh08nLFZf8hMk4mdJDX5rWBVlsCbKvRlWcLwNRU6+rIPcLx21x191kJgP8udtoZuHt5yUDWtgg==
        key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        jwtParser = Jwts.parserBuilder().requireIssuer(ISSUER)
                .setSigningKey(key).setAllowedClockSkewSeconds(10)
                .build();
    }

    public String generateToken(String username) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = LocalDateTime.now().plusHours(1); // TOKEN一个小时有效

        return Jwts.builder()
                .setSubject(username)
                .setIssuer(ISSUER)
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(key)
                .compact();
    }

    public Jws<Claims> parseToken(String token) {
        try {
            return jwtParser.parseClaimsJws(token);
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException |
                IllegalArgumentException e) {
            throw new BadCredentialsException("Invalid Token", e);
        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException("Token Expired", e);
        }
    }
}
