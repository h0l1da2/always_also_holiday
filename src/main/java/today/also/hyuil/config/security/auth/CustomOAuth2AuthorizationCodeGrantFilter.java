package today.also.hyuil.config.security.auth;

import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationCodeGrantFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import today.also.hyuil.config.security.auth.userinfo.SnsInfo;
import today.also.hyuil.config.security.jwt.CustomAccessTokenResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.springframework.http.MediaType.*;

public class CustomOAuth2AuthorizationCodeGrantFilter extends OAuth2AuthorizationCodeGrantFilter {

    private final SnsInfo snsInfo;

    /**
     * Constructs an {@code OAuth2AuthorizationCodeGrantFilter} using the provided
     * parameters.
     *
     * @param clientRegistrationRepository the repository of client registrations
     * @param authorizedClientRepository   the authorized client repository
     * @param authenticationManager        the authentication manager
     * @param snsInfo
     */
    public CustomOAuth2AuthorizationCodeGrantFilter(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository authorizedClientRepository, AuthenticationManager authenticationManager, SnsInfo snsInfo) {
        super(clientRegistrationRepository, authorizedClientRepository, authenticationManager);
        this.snsInfo = snsInfo;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("필터 시작");
        // http://localhost:8080/fanLetter?code={code}&state={state}

        String code = request.getParameter("code");

        if (!StringUtils.hasText(code)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String requestURI = request.getRequestURL().toString();
        String state = request.getParameter("state");

        HttpSession session = request.getSession();
        if (session != null) {
            String originState = String.valueOf(session.getAttribute("state"));

            // state가 동일하고, 정상 응답이 왔을 경우(코드 받았음)
            if (state.equals(originState) && StringUtils.hasText(code)) {
                // state 값이 key 고 value 가 sns 명
                String sns = String.valueOf(session.getAttribute(originState));
                String clientId = snsInfo.clientId(sns);
                String clientSecret = snsInfo.clientSecret(sns);
                String tokenUri = snsInfo.tokenUri(sns);

                MultiValueMap<String, String> parameters =
                        setParameters(code, clientId, clientSecret, requestURI);

                // 헤더 세팅
                HttpHeaders headers = new HttpHeaders();
                headers.setBasicAuth(sns, snsInfo.clientSecret(sns));
                headers.setContentType(valueOf(APPLICATION_FORM_URLENCODED_VALUE));

                HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(parameters, headers);

                // https://kauth.kakao.com/oauth/token 으로 토큰 요청 보내기
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<CustomAccessTokenResponse> responseEntity =
                        restTemplate.exchange(tokenUri, HttpMethod.POST, entity, CustomAccessTokenResponse.class);

                // (토큰) 응답 받기
                CustomAccessTokenResponse tokenResponse = responseEntity.getBody();
                request.setAttribute("tokenResponse", tokenResponse);

            }
        }



        String errorDescription = request.getParameter("errorDescription");
        String error = request.getParameter("error");

        if (StringUtils.hasText(error) | StringUtils.hasText(errorDescription)) {
            response.sendRedirect("/loginForm?error=error");
        } else {
            filterChain.doFilter(request, response);
        }


    }

    private MultiValueMap<String, String> setParameters(String code, String clientId, String clientSecret, String requestURI) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", "authorization_code");
        parameters.add("code", code);
        parameters.add("client_id", clientId);
        parameters.add("client_secret", clientSecret);
        parameters.add("redirect_uri", requestURI);
        return parameters;
    }
}