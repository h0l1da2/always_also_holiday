package today.also.hyuil.config.security.auth;

import today.also.hyuil.domain.member.Sns;

public interface SnsUserInfo {

    String getPkey();
    Sns getSnsName();
    String getEmail();
    String getName();
    String getMobile();
}
