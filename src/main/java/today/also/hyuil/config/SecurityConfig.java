package today.also.hyuil.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import today.also.hyuil.config.security.CustomUserDetailsService;
import today.also.hyuil.config.security.jwt.JwtFilter;
import today.also.hyuil.config.security.jwt.JwtTokenParser;
import today.also.hyuil.config.security.jwt.JwtTokenProvider;
import today.also.hyuil.repository.member.MemberRepository;
import today.also.hyuil.repository.security.JwtTokenRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberRepository memberRepository;
    private final JwtTokenParser jwtTokenParser;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenRepository jwtTokenRepository;

    public SecurityConfig(MemberRepository memberRepository, JwtTokenParser jwtTokenParser, JwtTokenProvider jwtTokenProvider, JwtTokenRepository jwtTokenRepository) {
        this.memberRepository = memberRepository;
        this.jwtTokenParser = jwtTokenParser;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtTokenRepository = jwtTokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .mvcMatchers("/fanLetter")
                .hasRole("USER")
                .anyRequest()
                .permitAll()

                .and()
                .addFilterBefore(new JwtFilter(jwtTokenParser, jwtTokenProvider, userDetailsService()), UsernamePasswordAuthenticationFilter.class)
//                .exceptionHandling()
//                .authenticationEntryPoint() // 인증이 실패했을 경우
//                .accessDeniedHandler() // 권한이 없을 경우
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .mvcMatchers("/static/**", "/favicon.ico");
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(memberRepository);
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
