package today.also.hyuil.config.security.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import today.also.hyuil.domain.security.Token;
import today.also.hyuil.repository.security.JwtTokenRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenParser jwtTokenParser;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenRepository jwtTokenRepository;
    private static final String BEARER = "Bearer_";

    public JwtFilter(JwtTokenParser jwtTokenParser, JwtTokenProvider jwtTokenProvider, JwtTokenRepository jwtTokenRepository) {
        this.jwtTokenParser = jwtTokenParser;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtTokenRepository = jwtTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        if (token != null) {
            token.substring(BEARER.length());
            boolean tokenValid = jwtTokenParser.validToken(token, false);

            if (!tokenValid) {
                Token refreshToken = jwtTokenParser.getRefreshToken(token);
                boolean refreshValid = jwtTokenParser.validToken(refreshToken.getToken(), true);
                if (refreshValid) {
                    Claims claims = jwtTokenParser.getClaims(token);
                    String refresh = jwtTokenProvider.createRefreshToken();
                    String memberId = claims.get("memberId", String.class);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.toLowerCase().startsWith(BEARER.toLowerCase())) {
            return bearerToken.substring(BEARER.length()).trim();
        }

        return null;
    }
}
