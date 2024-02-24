package today.also.hyuil.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import today.also.hyuil.member.repository.MemberJpaRepository;
import today.also.hyuil.member.repository.MemberRepository;
import today.also.hyuil.member.service.MemberJoinServiceImpl;
import today.also.hyuil.member.service.MemberJoinService;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final MemberRepository memberRepository;
    private final MemberJpaRepository memberJpaRepository;

    // TODO 시큐리티 컨피그에 옮기면?
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MemberJoinService memberJoinService() {
        return new MemberJoinServiceImpl(memberRepository, memberJpaRepository,bCryptPasswordEncoder());
    }


}
