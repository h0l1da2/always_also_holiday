package today.also.hyuil.config.security.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import today.also.hyuil.config.security.jwt.JwtTokenProvider;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.repository.member.MemberRepository;

import java.util.ArrayList;
import java.util.Collection;

public class CustomOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    public CustomOAuth2AuthorizedClientService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, String principalName) {



        return null;
    }

    private Collection<GrantedAuthority> putRoleToAuthorities(Member findMember) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(findMember.getRole().getName().toString()));
        return authorities;
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        String clientId = authorizedClient.getClientRegistration().getRegistrationId();
    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {

    }
}
