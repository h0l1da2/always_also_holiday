package today.also.hyuil.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.service.member.inter.MemberJoinService;

public class CustomUserDetailsService implements UserDetailsService {

    private final MemberJoinService memberJoinService;

    public CustomUserDetailsService(MemberJoinService memberJoinService) {
        this.memberJoinService = memberJoinService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberJoinService.findMyAccount(username);

        if (member == null) {
            throw new UsernameNotFoundException("아이디가 없음");
        }

        return new CustomUserDetails(member);
    }
}
