package today.also.hyuil.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.service.member.inter.MemberJoinService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberJoinService memberJoinService;

    public CustomUserDetailsService(MemberJoinService memberJoinService) {
        this.memberJoinService = memberJoinService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("그냥 유저디테일서비스");
        Member member = memberJoinService.findMyAccountMemberId(username);

        if (member == null) {
            throw new UsernameNotFoundException("아이디가 없음");
        }

        return new CustomUserDetails(member);
    }
}
