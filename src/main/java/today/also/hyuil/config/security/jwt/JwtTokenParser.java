package today.also.hyuil.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import today.also.hyuil.domain.security.Token;
import today.also.hyuil.repository.security.JwtTokenRepository;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Component
public class JwtTokenParser {

    private final JwtParser jwtParser;
    private final JwtTokenRepository jwtTokenRepository;
    private static final String BEARER = "Bearer ";


    public JwtTokenParser(@Value("${token.secret.key}") String secretKey, JwtTokenRepository jwtTokenRepository) {
        this.jwtTokenRepository = jwtTokenRepository;
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public boolean validToken(String token, boolean isRefreshToken) {
        Claims claims = getClaims(token);
        if (claims.getSubject() != null | !isRefreshToken) {
            return !claims.getExpiration().before(new Date());
        }
        return false;
    }

    public Token getRefreshToken(String accessToken) {
        Claims claims = getClaims(accessToken);
        if (claims.getSubject() != null) {
            String memberId = claims.get("memberId", String.class);
            Token refreshToken = jwtTokenRepository.findByMemberId(memberId);
            return refreshToken;
        }
        return null;
    }

    public Collection<GrantedAuthority> getAuthorities(String accessToken) {
        Claims claims = getClaims(accessToken);
        String role = getRoleToString(claims);

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));

        return authorities;
    }

    private String getRoleToString(Claims claims) {
        List<Map<String, String>> roles = (List<Map<String, String>>) claims.get("role");
        return roles.get(0).get("authority");
    }

    public Claims getClaims(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return claims;
    }

    public String tokenInMemberId(String token) {
        Claims claims = getClaims(token);
        return String.valueOf(claims.get("memberId"));
    }

    public String getTokenHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.toLowerCase().startsWith(BEARER.toLowerCase())) {
            return bearerToken.substring(BEARER.length()).trim();
        }
        return null;
    }

    public String getTokenUrl(HttpServletRequest request) {
        String token = request.getParameter("token");
        System.out.println("token1 = " + token);
        if (StringUtils.hasText(token)) {
            System.out.println("token2 = " + token);
            return token;
        }
        return null;
    }

}
