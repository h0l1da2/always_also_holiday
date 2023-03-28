package today.also.hyuil.config.security.auth.userinfo;

import today.also.hyuil.domain.member.Sns;

import java.util.Map;

public class NaverUserInfo implements SnsUserInfo{

    private final Map<String, Object> attributes;

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getPkey() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getMemberId() {
        return getSnsName() + getPkey();
    }

    @Override
    public Sns getSnsName() {
        return Sns.NAVER;
    }

    @Override
    public String getEmail() {
        return String.valueOf(attributes.get("email"));
    }

    @Override
    public String getName() {
        return String.valueOf(attributes.get("name"));
    }

    @Override
    public String getMobile() {
        return String.valueOf(attributes.get("mobile"));
    }
}
