package today.also.hyuil.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import today.also.hyuil.config.security.CustomUserDetailsService;
import today.also.hyuil.repository.member.MemberRepository;
import today.also.hyuil.service.member.MemberJoinServiceImpl;
import today.also.hyuil.service.member.inter.MemberJoinService;

@Configuration
public class AppConfig {

    private final MemberRepository memberRepository;

    public AppConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(memberJoinService());
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MemberJoinService memberJoinService() {
        return new MemberJoinServiceImpl(memberRepository, bCryptPasswordEncoder());
    }


}
