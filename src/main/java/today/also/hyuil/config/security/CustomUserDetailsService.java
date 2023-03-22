package today.also.hyuil.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.repository.member.MemberRepository;

public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberIdRole(username);

        if (member == null) {
            throw new UsernameNotFoundException("아이디가 없음");
        }

        return new CustomUserDetails(member);
    }
}
