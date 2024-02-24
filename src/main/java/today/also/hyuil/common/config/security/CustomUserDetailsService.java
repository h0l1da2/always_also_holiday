package today.also.hyuil.common.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import today.also.hyuil.member.domain.Member;
import today.also.hyuil.member.service.MemberJoinService;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberJoinService memberJoinService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("CustomUserDetailsService");
        Member member = memberJoinService.findMyAccount(Long.parseLong(username));

        if (member == null) {
            throw new UsernameNotFoundException("아이디가 없음");
        }

        return new CustomUserDetails(member);
    }
}
