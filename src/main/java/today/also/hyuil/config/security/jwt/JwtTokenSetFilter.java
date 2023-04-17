package today.also.hyuil.config.security.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenSetFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Jwt 토큰 셋팅 필터");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            System.out.println("토큰이 널이 아니네요?");
            String accessToken = String.valueOf(authentication.getCredentials());
            if (StringUtils.hasText(accessToken)) {
                response.setHeader("Authorization", "Bearer "+accessToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
