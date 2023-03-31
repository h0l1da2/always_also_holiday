package today.also.hyuil.config.security.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationCodeGrantFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.util.StringUtils;
import today.also.hyuil.config.security.auth.userinfo.SnsInfo;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CustomOAuth2AuthorizationCodeGrantFilter extends OAuth2AuthorizationCodeGrantFilter {

    private final SnsInfo snsInfo;

    public CustomOAuth2AuthorizationCodeGrantFilter(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository authorizedClientRepository, AuthenticationManager authenticationManager, SnsInfo snsInfo) {
        super(clientRegistrationRepository, authorizedClientRepository, authenticationManager);
        this.snsInfo = snsInfo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String requestURI = request.getRequestURL().toString();

        String clientId = snsInfo.checkClientId(requestURI);

        HttpSession session = request.getSession();
        if (session != null) {
            String originState = String.valueOf(session.getAttribute("state"));
            if (state.equals(originState) && StringUtils.hasText(code)) {
                String tokenUri = snsInfo.tokenUri(clientId);
                response.sendRedirect(tokenUri);
            }
        }


        String errorDescription = request.getParameter("errorDescription");
        String error = request.getParameter("error");

        if (StringUtils.hasText(error) | StringUtils.hasText(errorDescription)) {
            response.sendRedirect("/loginForm?error=error");
        }

        filterChain.doFilter(request, response);

    }
}
