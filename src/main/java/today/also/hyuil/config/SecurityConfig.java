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
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import today.also.hyuil.config.security.CustomAccessDeniedHandler;
import today.also.hyuil.config.security.CustomAuthenticationEntryPoint;
import today.also.hyuil.config.security.CustomUserDetailsService;
import today.also.hyuil.config.security.auth.CustomDefaultOAuth2UserService;
import today.also.hyuil.config.security.auth.CustomOAuth2SuccessHandler;
import today.also.hyuil.config.security.jwt.*;
import today.also.hyuil.repository.member.MemberRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberRepository memberRepository;
    private final JwtTokenParser jwtTokenParser;
    private final JwtAuthService jwtAuthService;
    private final JwtTokenService jwtTokenService;

    public SecurityConfig(MemberRepository memberRepository, JwtTokenParser jwtTokenParser, JwtAuthService jwtAuthService, JwtTokenService jwtTokenService) {
        this.memberRepository = memberRepository;
        this.jwtTokenParser = jwtTokenParser;
        this.jwtAuthService = jwtAuthService;
        this.jwtTokenService = jwtTokenService;
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
                .addFilterBefore(new JwtFilter(userDetailsService(), jwtTokenParser, jwtTokenService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtFilter(userDetailsService(), jwtTokenParser, jwtTokenService), OAuth2LoginAuthenticationFilter.class)
                .addFilterAfter(new JwtTokenSetFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // 인증이 실패했을 경우
                .accessDeniedHandler(new CustomAccessDeniedHandler()) // 권한이 없을 경우

                .and()
                .oauth2Login()
                .loginPage("/loginForm")
                .userInfoEndpoint()
                .userService(new CustomDefaultOAuth2UserService(memberRepository, bCryptPasswordEncoder(), jwtAuthService)) // 로그인
                .and()
                .successHandler(new CustomOAuth2SuccessHandler(jwtTokenService, memberRepository))
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
