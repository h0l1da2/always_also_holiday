package today.also.hyuil.config.security.auth.userinfo;

import today.also.hyuil.domain.member.Sns;

import java.util.Map;

public class KakaoUserInfo implements SnsUserInfo {

    private final Map<String, Object> attributes;
    private final Map<String, Object> attributesAccount;
    private final Map<String, Object> attributesProfile;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.attributesAccount = (Map<String, Object>) attributes.get("kakao_account");
        this.attributesProfile = (Map<String, Object>) attributesAccount.get("profile");
    }
    @Override
    public String getMemberId() {
        return getSnsName() + getPkey();
    }
    @Override
    public String getPkey() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public Sns getSnsName() {
        return Sns.KAKAO;
    }

    @Override
    public String getEmail() {
        return String.valueOf(attributesAccount.get("email"));
    }

    @Override
    public String getName() {
        return String.valueOf(attributesProfile.get("nickname"));
    }

    @Override
    public String getMobile() {
        return "NONE";
    }
}
