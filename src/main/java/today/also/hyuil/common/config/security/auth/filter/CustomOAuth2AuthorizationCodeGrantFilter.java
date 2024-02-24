package today.also.hyuil.common.config.security.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationCodeGrantFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import today.also.hyuil.common.config.security.auth.tokenresponse.TokenResponse;
import today.also.hyuil.common.config.security.auth.userinfo.SnsInfo;
import today.also.hyuil.member.domain.type.Sns;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.valueOf;

@Slf4j
public class CustomOAuth2AuthorizationCodeGrantFilter extends OAuth2AuthorizationCodeGrantFilter {

    private final SnsInfo snsInfo;

    public CustomOAuth2AuthorizationCodeGrantFilter(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository authorizedClientRepository, AuthenticationManager authenticationManager, SnsInfo snsInfo) {
        super(clientRegistrationRepository, authorizedClientRepository, authenticationManager);
        this.snsInfo = snsInfo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("그랜트 필터 시작");

        // https://alwaysalsoholiday.com/fanLetter?code={code}&state={state}
        String code = request.getParameter("code");

        if (!StringUtils.hasText(code)) {
            log.info("코드 없음");
            filterChain.doFilter(request, response);
            return;
        }

        String requestURI = request.getRequestURL().toString();

        HttpSession session = request.getSession();

        if (session != null) {
            String originState = String.valueOf(session.getAttribute("state"));
            String state = request.getParameter("state");

            if (originState == null | state == null) {
                filterChain.doFilter(request, response);
                return;
            }

            if (state.equals(originState) && StringUtils.hasText(code)) {
                log.info("Authentication 생성");
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
            }
        }



        filterChain.doFilter(request, response);


    }

    private void removeSessionAttr(HttpSession session, String originState) {
        session.removeAttribute("state");
        session.removeAttribute(originState);
    }

    private MultiValueMap<String, String> setParameters(String code, String clientId, String clientSecret, String requestURI) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        String redirectUri = requestURI;
        System.out.println("redirectUri = " + redirectUri);
        parameters.add("grant_type", "authorization_code");
        parameters.add("code", code);
        parameters.add("client_id", clientId);
        parameters.add("client_secret", clientSecret);
        parameters.add("redirect_uri", redirectUri);
        return parameters;
    }
}
