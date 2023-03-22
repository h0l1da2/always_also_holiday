package today.also.hyuil.config.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import today.also.hyuil.domain.security.Token;
import today.also.hyuil.repository.security.JwtTokenRepository;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Component
@PropertySource("classpath:application.yml")
public class JwtTokenProvider {

    @Value("${token.valid.time}")
    private int tokenValidTime;
    @Value("${token.access}")

    private String accessToken;
    @Value("${token.refresh}")

    private String refreshToken;
    private final Key key;
    private final JwtTokenRepository jwtTokenRepository;

    public JwtTokenProvider(@Value("${token.secret.key}") String secretKey, JwtTokenRepository jwtTokenRepository) {
        this.jwtTokenRepository = jwtTokenRepository;
        // secretKey base64로 디코딩
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // key 객체로 변환
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String memberId, Collection<? extends GrantedAuthority> role) {
        Claims claims = getClaims(accessToken, tokenValidTime);

        claims.put("memberId", memberId);
        claims.put("role", role);

        return Jwts.builder()
                .setHeaderParam("typ", "Bearer")
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String createRefreshToken() {
        Claims claims = getClaims(refreshToken, tokenValidTime * 10);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Token saveRefreshInDB(Token refreshToken) {
        return jwtTokenRepository.insertRefreshToken(refreshToken);
    }

    private Claims getClaims(String tokenType,int tokenValidTime) {
        Date now = new Date();

        return Jwts.claims()
                        .setSubject(tokenType)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + tokenValidTime));

    }


    public String reCreateJwtToken(String token) {
        Jwt<Header, Claims> headerClaimsJwt =
                Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJwt(token);

        Claims claims = headerClaimsJwt.getBody();

        String memberId = claims.get("memberId", String.class);
        String role = claims.get("role", String.class);

        return createAccessToken(memberId, getAuthorities(role));
    }

    private Collection<GrantedAuthority> getAuthorities(String role) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(
                new SimpleGrantedAuthority(
                        String.valueOf(role)
                ));
        return authorities;
    }

    public Token duplicationTokenDB(String memberId) {
        return jwtTokenRepository.findByMemberId(memberId);
    }

    public Token setNewToken(Token token) {
        return jwtTokenRepository.updateNewToken(token);
    }
}

