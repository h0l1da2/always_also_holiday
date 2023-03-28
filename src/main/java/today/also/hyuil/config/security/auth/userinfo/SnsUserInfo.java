package today.also.hyuil.config.security.auth.userinfo;

import today.also.hyuil.domain.member.Sns;

public interface SnsUserInfo {

    String getPkey();
    String getMemberId();
    Sns getSnsName();
    String getEmail();
    String getName();
    String getMobile();
}
