package today.also.hyuil.common.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import today.also.hyuil.common.exception.BadRequestException;
import today.also.hyuil.common.exception.Error;
import today.also.hyuil.member.domain.Member;
import today.also.hyuil.member.service.MemberJoinService;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberJoinService memberJoinService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("CustomUserDetailsService");
        Member member = memberJoinService.findMyAccount(Long.parseLong(username));

        if (member == null) {
            throw new BadRequestException(Error.MEMBER_NOT_FOUND);
        }

        return new CustomUserDetails(member);
    }
}
