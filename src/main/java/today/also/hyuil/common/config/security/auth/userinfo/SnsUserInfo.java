package today.also.hyuil.common.config.security.auth.userinfo;

import today.also.hyuil.member.domain.type.Sns;

public interface SnsUserInfo {

    String getPkey();
    String getMemberId();
    Sns getSnsName();
    String getEmail();
    String getName();
    String getMobile();
}
