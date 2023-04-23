package today.also.hyuil.config.security.auth.filter;

import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationCodeGrantFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import today.also.hyuil.config.security.auth.CustomDefaultOAuth2UserService;
import today.also.hyuil.config.security.auth.userinfo.SnsInfo;
import today.also.hyuil.config.security.auth.tokenresponse.TokenResponse;
import today.also.hyuil.domain.member.Sns;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

import static org.springframework.http.MediaType.*;

public class CustomOAuth2AuthorizationCodeGrantFilter extends OAuth2AuthorizationCodeGrantFilter {

    private final String BASE_URL = "http://localhost:8080";
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final CustomDefaultOAuth2UserService customDefaultOAuth2UserService;
    private final SnsInfo snsInfo;

    public CustomOAuth2AuthorizationCodeGrantFilter(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository authorizedClientRepository, AuthenticationManager authenticationManager, CustomDefaultOAuth2UserService customDefaultOAuth2UserService, SnsInfo snsInfo) {
        super(clientRegistrationRepository, authorizedClientRepository, authenticationManager);
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.customDefaultOAuth2UserService = customDefaultOAuth2UserService;
        this.snsInfo = snsInfo;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("그랜트 필터 시작");
        // http://localhost:8080/fanLetter?code={code}&state={state}
        String code = request.getParameter("code");

        if (!StringUtils.hasText(code)) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestURI = request.getRequestURL().toString();

        HttpSession session = request.getSession(false);

        if (session != null) {
            String originState = String.valueOf(session.getAttribute("state"));
            String state = request.getParameter("state");

            if (state.equals(originState) && StringUtils.hasText(code)) {
                /**
                 * 카카오 -
                 * state가 동일하고, 정상 응답이 왔을 경우(코드 받았음)
                 */
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
                if (sns.equals(Sns.GOOGLE.name())) {
                    String authValue =
                            Base64.getEncoder().encodeToString(
                                    (clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
                    headers.set("Authorization", "Basic "+authValue);
                }

                HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(parameters, headers);

                // https://kauth.kakao.com/oauth/token 으로 토큰 요청 보내기
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<TokenResponse> responseEntity =
                        restTemplate.exchange(tokenUri, HttpMethod.POST, entity, TokenResponse.class);

                // (토큰) 응답 받기
                // 카카오, 네이버, 구글이 다 다름 TokenResponse 가
                TokenResponse tokenResponse = responseEntity.getBody();
                request.setAttribute("tokenResponse", tokenResponse);
                request.setAttribute("sns", sns);

                removeSessionAttr(session, originState);

                // authentication 생성 후, SpringContext에 저장하는 작업
                ClientRegistration clientRegistration =
                        clientRegistrationRepository.findByRegistrationId(sns.toLowerCase());
                OAuth2AccessToken oAuth2AccessToken = getOAuth2AccessToken(tokenResponse);

                OAuth2UserRequest oAuth2UserRequest = new OAuth2UserRequest(clientRegistration, oAuth2AccessToken);

                String accessToken = tokenResponse.getAccessToken();
                Authentication authentication = getAuthentication(accessToken, oAuth2UserRequest);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        }



        filterChain.doFilter(request, response);


    }

    private OAuth2AccessToken getOAuth2AccessToken(TokenResponse tokenResponse) {
        String accessToken = tokenResponse.getAccessToken();
        long expiresIn = tokenResponse.getExpiresIn();
        Instant expiresAt = Instant.now().plusSeconds(expiresIn);
        return new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, accessToken, Instant.now(), expiresAt);
    }

    private void removeSessionAttr(HttpSession session, String originState) {
        session.removeAttribute("state");
        session.removeAttribute(originState);
    }
    private Authentication getAuthentication(String accessToken, OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User oAuth2User = customDefaultOAuth2UserService.loadUser(oAuth2UserRequest);
        return new UsernamePasswordAuthenticationToken(oAuth2User, accessToken, oAuth2User.getAuthorities());
    }
    private MultiValueMap<String, String> setParameters(String code, String clientId, String clientSecret, String requestURI) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        String redirectUri = requestURI;
        parameters.add("grant_type", "authorization_code");
        parameters.add("code", code);
        parameters.add("client_id", clientId);
        parameters.add("client_secret", clientSecret);
        parameters.add("redirect_uri", redirectUri);
        return parameters;
    }
}
