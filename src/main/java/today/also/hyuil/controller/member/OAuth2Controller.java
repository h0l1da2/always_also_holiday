//package today.also.hyuil.controller.member;
//
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//import today.also.hyuil.config.security.auth.userinfo.SnsInfo;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@RestController
//public class OAuth2Controller {
//
//    private final SnsInfo snsInfo;
//    private final String BASE_URL = "http://localhost:8080";
//
//    public OAuth2Controller(SnsInfo snsInfo) {
//        this.snsInfo = snsInfo;
//    }
//
//    @GetMapping("/oauth2/authorization/{sns}")
//    public void snsRequest(@PathVariable String sns, HttpServletRequest request, HttpServletResponse response) {
//
//        /**
//         * sns 에 따라 다른 redirect_uri 가져오기
//         * 오류 Url 경우도...
//         * redirectUrl 파라미터로 추가
//         * sendRedirect 로 authorization-uri 로 보내기
//         *
//         * 1. /oauth/authorize 로 요청
//         * 카카오 oauth 서버에서
//         * - client_id , redirect_uri , response_type (code)
//         * - 인가 - scope 토큰 요청에 필요한 인가 코드 , state1
//         * - 에러 - error , error_description
//         * 2. redirect_uri (kakao : /login/oauth2/code/kakao) 여기로 보내줌
//         * 3. code 를 활용해서 토큰 발급
//         *
//         */
//
//        String clientId = snsInfo.clientId(sns);
//        String authUri = snsInfo.authUri(sns);
//        String redirectUri = snsInfo.redirectUri(sns);
//        String responseType = snsInfo.responseType();
//        List<String> scope = snsInfo.scope(sns);
//        redirectUri = getRedirectUri(request, redirectUri);
//
//        Map<String, Object> params =
//                paramToMap(clientId, redirectUri, responseType, scope);
//
//        String parameters =
//                params.entrySet().stream()
//                .map(m -> m.getKey() + "=" + m.getValue())
//                .collect(Collectors.joining("&"));
//
//
//        String authorizationUri = authUri + "?" + parameters;
//        System.out.println("authorizationUri = " + authorizationUri);
//        // authorization-uri 로 이동
//        try {
//            response.sendRedirect(authorizationUri);
//        } catch (IOException e) {
//            System.out.println("리디렉트 문제 = " + e);
//            e.printStackTrace();
//        }
//
//    }
//
//    private String getRedirectUri(HttpServletRequest request, String redirectUri) {
//        String redirectUrl = request.getParameter("redirectUrl");
//        if (StringUtils.hasText(redirectUrl)) {
//            redirectUri = BASE_URL + redirectUrl;
//        }
//        return redirectUri;
//    }
//
//    private Map<String, Object> paramToMap(String clientId, String redirectUri, String responseType, List<String> scope) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("client_id", clientId);
//        params.put("redirect_uri", redirectUri);
//        params.put("response_type", responseType);
//        params.put("scope", scope);
//        return params;
//    }
//
//}
