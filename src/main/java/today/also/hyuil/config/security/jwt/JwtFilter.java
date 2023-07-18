package today.also.hyuil.config.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import today.also.hyuil.domain.security.Token;
import today.also.hyuil.exception.HaveNotToken;
import today.also.hyuil.exception.TokenTimeOverException;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JWT 검증 필터 시작");

        try {
            String token = jwtTokenService.getToken(request);
            jwtTokenService.tokenValid(token, request, response);

            // 토큰s 재발급 후, 저장~
            Map<String, String> map = jwtTokenService.reCreateTokens(token);

            String accessT = map.get(TokenName.ACCESS_TOKEN.name());
            String refreshT = map.get(TokenName.REFRESH_TOKEN.name());
            Long id = Long.parseLong(map.get("id"));

            // 기존 리프레쉬 토큰이랑 바꿔치기 or 새로 insert
            jwtTokenService.saveRefreshInDB(new Token(id, refreshT));

            // 인증 완료!
            Authentication authentication = extractAuthentication(id, accessT);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("인증 완료 !");
            // TODO 토큰에 memberId -> id 들어가도록 변경
            setSessionId(request, id);

        } catch (ExpiredJwtException e) {
            log.info("인증 실패 : 토큰 만료");
            filterChain.doFilter(request, response);
            return;
        } catch (MalformedJwtException e) {
            log.info("인증 실패 : 토큰 값이 올바르지 않음. 비정상적인 토큰");
            filterChain.doFilter(request, response);
            return;
        } catch (HaveNotToken e) {
            log.info("인증 실패 : 토큰이 없음");
            filterChain.doFilter(request, response);
            return;
        } catch (TokenTimeOverException e) {
            log.info("인증 실패 : 토큰 시간 만료");
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void setSessionId(HttpServletRequest request, Long id) {
        HttpSession session = request.getSession();
        session.setAttribute("id", id);
    }

    private Authentication extractAuthentication(Long id, String accessToken) {
        try {
            Collection<GrantedAuthority> authorities = jwtTokenService.getAuthoritiesFromToken(accessToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(String.valueOf(id));

            return new UsernamePasswordAuthenticationToken(userDetails, accessToken, authorities);
        } catch (JwtException | IllegalArgumentException | NullPointerException exception) {
            throw new BadCredentialsException(exception.getMessage());
        }
    }



}
