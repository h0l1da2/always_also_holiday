package today.also.hyuil.config.security.auth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomDefaultOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        SnsUserInfo userInfo = null;

        String client = userRequest.getClientRegistration().getRegistrationId();

        // userInfo 에 해당 sns 에 맞는 info 객체를 넣는 메소드
        clientUserInfoCheck(oAuth2User, client);

//        // sns이름 + pk앞 5자리가 sns아이디
//        String pkey = userInfo.getPkey()
//                .substring(0, 6);
//
//        String memberId = userInfo.getSnsName() + pkey;


        return null;
    }

    private static void clientUserInfoCheck(OAuth2User oAuth2User, String client) {
        SnsUserInfo userInfo;
        if(client.equals("google")) {
            userInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        if(client.equals("naver")) {
            userInfo = new NaverUserInfo(
                    (Map<String, Object>) oAuth2User.getAttributes().get("response"));
        }
        if(client.equals("kakao")) {
            userInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }
    }
}
