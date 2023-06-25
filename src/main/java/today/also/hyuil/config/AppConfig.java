package today.also.hyuil.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import today.also.hyuil.repository.member.MemberJpaRepository;
import today.also.hyuil.repository.member.MemberRepository;
import today.also.hyuil.service.member.MemberJoinServiceImpl;
import today.also.hyuil.service.member.inter.MemberJoinService;

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
