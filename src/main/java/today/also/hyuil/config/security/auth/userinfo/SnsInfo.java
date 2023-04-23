package today.also.hyuil.config.security.auth.userinfo;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import today.also.hyuil.domain.member.Sns;

import java.util.ArrayList;
import java.util.List;

// Sns Info 나누기
@Component
@PropertySource("classpath:application.yml")
@Getter
public class SnsInfo {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;
    @Value("${spring.security.oauth2.client.provider.google.authorization-uri}")
    private String googleAuthUri;
    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String googleTokenUri;
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;
    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String naverRedirectUri;
    @Value("${spring.security.oauth2.client.provider.naver.authorization-uri}")
    private String naverAuthUri;
    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String naverTokenUri;
    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String naverProfileApi;


    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;
    @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
    private String kakaoAuthUri;
    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String kakaoTokenUri;


    public String clientId(String sns) {
        if (sns.equals(Sns.GOOGLE.name())) {
            return googleClientId;
        }
        if (sns.equals(Sns.KAKAO.name())) {
            return kakaoClientId;
        }
        if (sns.equals(Sns.NAVER.name())) {
            return naverClientId;
        }
        return null;
    }

    public String clientSecret(String sns) {
        if (sns.equals(Sns.GOOGLE.name())) {
            return googleClientSecret;
        }
        if (sns.equals(Sns.KAKAO.name())) {
            return kakaoClientSecret;
        }
        if (sns.equals(Sns.NAVER.name())) {
            return naverClientSecret;
        }
        return null;
    }

    public String redirectUri(String sns) {
        if (sns.equals(Sns.GOOGLE.name())) {
            return googleRedirectUri;
        }
        if (sns.equals(Sns.KAKAO.name())) {
            return kakaoRedirectUri;
        }
        if (sns.equals(Sns.NAVER.name())) {
            return naverRedirectUri;
        }
        return null;
    }

    public String authUri(String sns) {
        if (sns.equals(Sns.GOOGLE.name())) {
            return googleAuthUri;
        }
        if (sns.equals(Sns.KAKAO.name())) {
            return kakaoAuthUri;
        }
        if (sns.equals(Sns.NAVER.name())) {
            return naverAuthUri;
        }
        return null;
    }
    public String tokenUri(String sns) {
        if (sns.equals(Sns.GOOGLE.name())) {
            return googleTokenUri;
        }
        if (sns.equals(Sns.KAKAO.name())) {
            return kakaoTokenUri;
        }
        if (sns.equals(Sns.NAVER.name())) {
            return naverTokenUri;
        }
        return null;
    }

    public List<String> scope(String sns) {
        List<String> scope = new ArrayList<>();

        if (sns.equals(Sns.GOOGLE.name())) {
            scope.add("email");
            scope.add("profile");
        }
        if (sns.equals(Sns.KAKAO.name())) {
            scope.add("profile_nickname");
            scope.add("account_email");
            scope.add("openid");
        }
        if (sns.equals(Sns.NAVER.name())) {
            scope.add("name");
            scope.add("email");
            scope.add("mobile");
        }
        return scope;
    }

    public String checkClientId(String requestUrl) {
        if (requestUrl.startsWith(googleRedirectUri)) {
            return Sns.GOOGLE.name();
        }
        if (requestUrl.startsWith(googleRedirectUri)) {
            return Sns.KAKAO.name();
        }
        if (requestUrl.startsWith(googleRedirectUri)) {
            return Sns.NAVER.name();
        }
        return "";
    }

    public String responseType() {
        return "code";
    }
}
