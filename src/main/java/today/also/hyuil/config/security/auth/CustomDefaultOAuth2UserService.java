package today.also.hyuil.config.security.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import today.also.hyuil.config.security.CustomUserDetails;
import today.also.hyuil.config.security.jwt.JwtAuthService;
import today.also.hyuil.domain.member.*;
import today.also.hyuil.repository.member.MemberRepository;

import java.util.Date;
import java.util.UUID;

public class CustomDefaultOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtAuthService jwtAuthService;

    public CustomDefaultOAuth2UserService(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder, JwtAuthService jwtAuthService) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtAuthService = jwtAuthService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String client = userRequest.getClientRegistration().getRegistrationId();

        // userInfo 에 해당 sns 에 맞는 info 객체를 넣는 메소드
        SnsUserInfo userInfo = jwtAuthService.clientUserInfoCheck(oAuth2User, client);

        // sns이름 + pk가 memberId
        String pkey = userInfo.getPkey();
        String memberId = userInfo.getMemberId();
        Member member = memberRepository.findByMemberIdRole(memberId);

        // 기존 가입 멤버가 아니라면 ?
        if (member == null) {
            String nickname = userInfo.getSnsName()+"_"+pkey.substring(0, 6);
            String password = getEncodedPassword(pkey+ UUID.randomUUID());
            String name = userInfo.getName();
            String email = userInfo.getEmail();
            String phone = userInfo.getMobile();
            Sns sns = userInfo.getSnsName();
            Date now = new Date();
            Member newMember =
                    getNewSnsMember
                            (memberId, nickname, password, name, email, phone, sns, now);

            member = memberRepository.insertMember(newMember);
        }

        return new CustomUserDetails(member, oAuth2User.getAttributes());
    }

    private Member getNewSnsMember(String memberId, String nickname, String password, String name, String email, String phone, Sns sns, Date now) {
        return Member.builder()
                .memberId(memberId)
                .password(password)
                .name(name)
                .nickname(nickname)
                .email(email)
                .phone(phone)
                .address(new Address())
                .role(new Role(Name.ROLE_USER))
                .lastLogin(now)
                .joinDate(now)
                .pwdModifyDate(now)
                .sns(sns)
                .build();
    }



    private String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
