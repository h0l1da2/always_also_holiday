package today.also.hyuil.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import today.also.hyuil.domain.security.Token;
import today.also.hyuil.repository.security.JwtTokenRepository;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Component
public class JwtTokenParser {

    @Value("${token.access}")

    private String accessToken;
    @Value("${token.refresh}")

    private String refreshToken;
    private final JwtParser jwtParser;
    private final UserDetailsService userDetailsService;
    private final JwtTokenRepository jwtTokenRepository;

    public JwtTokenParser(@Value("${token.secret.key}") String secretKey, UserDetailsService userDetailsService, JwtTokenRepository jwtTokenRepository) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenRepository = jwtTokenRepository;
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    // authentication 생성
    public Authentication extractAuthentication(String memberId, String accessToken) {
        try {
            Claims claims = getClaims(accessToken);
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(claims.get("role").toString()));

            UserDetails userDetails = userDetailsService.loadUserByUsername(memberId);

            return new UsernamePasswordAuthenticationToken(userDetails, accessToken, authorities);
        } catch (JwtException | IllegalArgumentException | NullPointerException exception) {
            throw new BadCredentialsException(exception.getMessage());
        }
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

    public Claims getClaims(String token) {
        Claims claims = jwtParser.parseClaimsJwt(token).getBody();
        return claims;
    }

}
