package today.also.hyuil.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Component
@PropertySource("classpath:application.yml")
public class JwtTokenProvider {

    @Value("${token.valid.time}")
    private int tokenValidTime;
    @Value("${token.access}")

    private String accessToken;
    @Value("${token.refresh}")

    private String refreshToken;
    private final JwtParser jwtParser;
    private final Key key;

    public JwtTokenProvider(@Value("${token.secret.key}") String secretKey) {
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public String createAccessToken(Long id, Collection<? extends GrantedAuthority> role) {
        Claims claims = getNewClaims(accessToken, tokenValidTime);

        claims.put("id", id);
        claims.put("role", role);

        return Jwts.builder()
                .setHeaderParam("typ", "Bearer")
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String createRefreshToken() {
        Claims claims = getNewClaims(refreshToken, tokenValidTime * 10);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private Claims getNewClaims(String tokenType, int tokenValidTime) {
        Date now = new Date();

        return Jwts.claims()
                        .setSubject(tokenType)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + tokenValidTime));

    }

    public String reCreateJwtToken(String token) {
        Claims claims = getOldClaims(token);

        Long id = claims.get("id", Long.class);
        String role = getRoleToString(claims);
        if (StringUtils.hasText(role)) {
            return createAccessToken(id, getAuthorities(role));
        }
        return null;
    }

    private String getRoleToString(Claims claims) {
        List<Map<String, String>> roles = (List<Map<String, String>>) claims.get("role");
        return roles.get(0).get("authority");
    }

    public Claims getOldClaims(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return claims;
    }

    private Collection<GrantedAuthority> getAuthorities(String role) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }

}

