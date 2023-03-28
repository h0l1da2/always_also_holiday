package today.also.hyuil.config.security.auth;

import today.also.hyuil.domain.member.Sns;

import java.util.Map;

public class GoogleUserInfo implements SnsUserInfo{

    private final Map<String, Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    @Override
    public String getMemberId() {
        return getSnsName() + getPkey();
    }
    @Override
    public String getPkey() {
        return String.valueOf(attributes.get("sub"));
    }

    @Override
    public Sns getSnsName() {
        return Sns.GOOGLE;
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
        return "NONE";
    }
}
