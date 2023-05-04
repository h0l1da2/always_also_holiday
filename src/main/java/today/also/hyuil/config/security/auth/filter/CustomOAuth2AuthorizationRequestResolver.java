package today.also.hyuil.config.security.auth.filter;

import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import today.also.hyuil.config.security.auth.userinfo.SnsInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class CustomOAuth2AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {
    private final String REQUEST_URL = "/oauth2/authorization/";
    private final String BASE_URL = "http://localhost:8080";
    private final SnsInfo snsInfo;

    public CustomOAuth2AuthorizationRequestResolver(SnsInfo snsInfo) {
        this.snsInfo = snsInfo;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        // /oauth2/** 요청일 때만 resolver 동작해야 함니다
        if (request.getRequestURI().startsWith(REQUEST_URL)) {
            String sns = request.getRequestURI().substring(REQUEST_URL.length());
            return this.resolve(request, sns);
        }
        return null;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        System.out.println("리졸버 시작");
        String sns = clientRegistrationId.toUpperCase();
        String state = UUID.randomUUID().toString();

        setStateSession(request, state);
        setClientSession(request, state, sns);

        String clientId = snsInfo.clientId(sns);
        String authUri = snsInfo.authUri(sns);
        String redirectUri = getRedirectUri(request, sns);
        String responseType = snsInfo.responseType();
        String[] scopes = snsInfo.scope(sns).stream().toArray(String[]::new);

        Map<String, Object> map = new HashMap<>();
        map.put(OAuth2ParameterNames.RESPONSE_TYPE, responseType);
        map.put("client", sns);

        return OAuth2AuthorizationRequest
                .authorizationCode()
                .clientId(clientId)
                .authorizationUri(authUri)
                .scope(scopes)
                .redirectUri(redirectUri)
                .state(state)
                .parameters(params -> params.putAll(map))
                .build();
    }

    private void setClientSession(HttpServletRequest request, String state, String sns) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.setAttribute(state, sns);
        }
    }

    private void setStateSession(HttpServletRequest request, String state) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.setAttribute("state", state);
        }
    }

    private String getRedirectUri(HttpServletRequest request, String sns) {
        String redirectUrl = request.getParameter("redirectUrl");
        if (!StringUtils.hasText(redirectUrl)) {
            redirectUrl = snsInfo.redirectUri(sns.toUpperCase());
        } else {
            if (redirectUrl.startsWith("/fanLetter")) {
                redirectUrl = "/fanLetter";
            }
            if (redirectUrl.startsWith("/market/buy")) {
                redirectUrl = "/market/buy";
            }
            redirectUrl = BASE_URL + redirectUrl;
        }
        return redirectUrl;
    }

}
