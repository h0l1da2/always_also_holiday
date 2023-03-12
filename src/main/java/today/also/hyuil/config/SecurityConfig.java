package today.also.hyuil.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import today.also.hyuil.config.security.CustomUserDetailsService;
import today.also.hyuil.service.member.MemberJoinService;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberJoinService memberJoinService;

    public SecurityConfig(MemberJoinService memberJoinService) {
        this.memberJoinService = memberJoinService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()

                .authorizeRequests()
                .anyRequest()
                .permitAll();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(memberJoinService);
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
