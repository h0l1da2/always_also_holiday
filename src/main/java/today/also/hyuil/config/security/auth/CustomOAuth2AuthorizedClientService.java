package today.also.hyuil.config.security.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import today.also.hyuil.config.security.jwt.JwtAuthService;
import today.also.hyuil.config.security.jwt.JwtTokenService;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.repository.member.MemberRepository;

import java.util.Map;

@Service
public class CustomOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService {

    private final CustomOAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository;
    private final JwtAuthService jwtAuthService;
    private final JwtTokenService jwtTokenService;
    // 나중에 얘 JwtTokenService 로 옮기게 수정
    private final MemberRepository memberRepository;
    public CustomOAuth2AuthorizedClientService(CustomOAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository, JwtAuthService jwtAuthService, JwtTokenService jwtTokenService, MemberRepository memberRepository) {
        this.oAuth2AuthorizedClientRepository = oAuth2AuthorizedClientRepository;
        this.jwtAuthService = jwtAuthService;
        this.jwtTokenService = jwtTokenService;
        this.memberRepository = memberRepository;
    }

    @Override
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, String principalName) {



        return null;
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        // SNS 공급자 확인
        String client = authorizedClient.getClientRegistration().getRegistrationId();
        OAuth2User oAuth2User = (OAuth2User) principal.getPrincipal();

        SnsUserInfo userInfo = jwtAuthService.clientUserInfoCheck(oAuth2User, client);
        Member member = memberRepository.findByMemberIdRole(userInfo.getMemberId());

        // 토큰 발급
        Map<String, String> tokens = jwtTokenService.getTokens(
                member.getMemberId(), member.getRole().getName()
        );
        String refreshToken = tokens.get("refreshToken");
        String accessToken = tokens.get("accessToken");

        // 각 토큰 저장
        jwtTokenService.saveRefreshToken(member.getMemberId(), refreshToken);

        // Context 에 authentication 저장


    }


    @Override
    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {

    }


}
