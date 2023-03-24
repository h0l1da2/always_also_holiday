package today.also.hyuil.config.security.jwt;

import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import today.also.hyuil.domain.security.Token;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenParser jwtTokenParser;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private static final String BEARER = "Bearer ";

    public JwtFilter(JwtTokenParser jwtTokenParser, JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenParser = jwtTokenParser;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenParser.getTokenHeader(request);
        if (token == null) {
            String tokenUrl = jwtTokenParser.getTokenUrl(request);
            if (tokenUrl == null) {
                System.out.println("토큰이 없어요");
                filterChain.doFilter(request, response);
                return;
            }
            // url 파라미터에 토큰이 있을 경우
            token = tokenUrl;
        }

        boolean tokenValid = jwtTokenParser.validToken(token, false);

        if (!tokenValid) {
            Token refreshToken = jwtTokenParser.getRefreshToken(token);
            if (refreshToken == null) {
                throw new NullPointerException("리프레쉬 토큰이 없음");
            }
            boolean refreshValid = jwtTokenParser.validToken(refreshToken.getToken(), true);
            if (!refreshValid) {
                System.out.println("리프레쉬 토큰 인증 실패");
                filterChain.doFilter(request, response);
                return;
            }
        }
        // 토큰s 재발급 후, 저장~
        String memberId = jwtTokenParser.tokenInMemberId(token);
        String refreshT = jwtTokenProvider.createRefreshToken();
        String accessT = jwtTokenProvider.reCreateJwtToken(token);

        // 기존 리프레쉬 토큰이랑 바꿔치기 or 새로 insert
        Token refreshToken = jwtTokenProvider.saveRefreshInDB(new Token(memberId, refreshT));

        // 인증 완료!
        Authentication authentication = extractAuthentication(memberId, accessT);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("인증 완료!!");
        filterChain.doFilter(request, response);
    }

    // authentication 생성
    private Authentication extractAuthentication(String memberId, String accessToken) {
        try {
            Collection<GrantedAuthority> authorities = jwtTokenParser.getAuthorities(accessToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(memberId);

            return new UsernamePasswordAuthenticationToken(userDetails, accessToken, authorities);
        } catch (JwtException | IllegalArgumentException | NullPointerException exception) {
            throw new BadCredentialsException(exception.getMessage());
        }
    }

}
