package today.also.hyuil.config.security.jwt;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import today.also.hyuil.config.security.auth.GoogleUserInfo;
import today.also.hyuil.config.security.auth.KakaoUserInfo;
import today.also.hyuil.config.security.auth.NaverUserInfo;
import today.also.hyuil.config.security.auth.SnsUserInfo;

import java.util.Map;

@Component
public class JwtAuthService {

    public SnsUserInfo clientUserInfoCheck(OAuth2User oAuth2User, String client) {
        if(client.equals("google")) {
            return new GoogleUserInfo(oAuth2User.getAttributes());
        }
        if(client.equals("naver")) {
            return new NaverUserInfo(
                    (Map<String, Object>) oAuth2User.getAttributes().get("response"));
        }
        if(client.equals("kakao")) {
            return new KakaoUserInfo(oAuth2User.getAttributes());
        }
        return null;
    }

}
