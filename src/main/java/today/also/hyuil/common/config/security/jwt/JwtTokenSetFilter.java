package today.also.hyuil.common.config.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtTokenSetFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("Jwt 토큰 셋팅 필터");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            log.info("토큰이 널이 아님.");
            String accessToken = String.valueOf(authentication.getCredentials());
            if (StringUtils.hasText(accessToken)) {
                response.setHeader("Authorization", "Bearer "+accessToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
