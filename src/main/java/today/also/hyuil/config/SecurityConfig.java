package today.also.hyuil.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import today.also.hyuil.config.security.CustomAccessDeniedHandler;
import today.also.hyuil.config.security.CustomAuthenticationEntryPoint;
import today.also.hyuil.config.security.CustomUserDetailsService;
import today.also.hyuil.config.security.auth.CustomDefaultOAuth2UserService;
import today.also.hyuil.config.security.auth.CustomOAuth2SuccessHandler;
import today.also.hyuil.config.security.auth.filter.CustomOAuth2AuthorizationCodeGrantFilter;
import today.also.hyuil.config.security.auth.filter.CustomOAuth2AuthorizationRequestResolver;
import today.also.hyuil.config.security.auth.filter.OAuth2JwtTokenFilter;
import today.also.hyuil.config.security.auth.jwk.GoogleJwk;
import today.also.hyuil.config.security.auth.jwk.KakaoJwk;
import today.also.hyuil.config.security.auth.userinfo.SnsInfo;
import today.also.hyuil.config.security.jwt.JwtFilter;
import today.also.hyuil.config.security.jwt.JwtTokenParser;
import today.also.hyuil.config.security.jwt.JwtTokenService;
import today.also.hyuil.config.security.jwt.JwtTokenSetFilter;
import today.also.hyuil.service.member.inter.MemberJoinService;
import today.also.hyuil.service.web.WebService;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final WebService webService;
    private final MemberJoinService memberJoinService;
    private final JwtTokenParser jwtTokenParser;
    private final JwtTokenService jwtTokenService;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomDefaultOAuth2UserService customDefaultOAuth2UserService;
    private final CustomOAuth2AuthorizationRequestResolver customOAuth2AuthorizationRequestResolver;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository;
    private final SnsInfo snsInfo;
    private final KakaoJwk kakaoJwk;
    private final GoogleJwk googleJwk;
    public SecurityConfig(WebService webService, MemberJoinService memberJoinService, JwtTokenParser jwtTokenParser, JwtTokenService jwtTokenService, CustomUserDetailsService customUserDetailsService, CustomDefaultOAuth2UserService customDefaultOAuth2UserService, CustomOAuth2AuthorizationRequestResolver customOAuth2AuthorizationRequestResolver, ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository, SnsInfo snsInfo, KakaoJwk kakaoJwk, GoogleJwk googleJwk) {
        this.webService = webService;
        this.memberJoinService = memberJoinService;
        this.jwtTokenParser = jwtTokenParser;
        this.jwtTokenService = jwtTokenService;
        this.customUserDetailsService = customUserDetailsService;
        this.customDefaultOAuth2UserService = customDefaultOAuth2UserService;
        this.customOAuth2AuthorizationRequestResolver = customOAuth2AuthorizationRequestResolver;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.oAuth2AuthorizedClientRepository = oAuth2AuthorizedClientRepository;
        this.snsInfo = snsInfo;
        this.kakaoJwk = kakaoJwk;
        this.googleJwk = googleJwk;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationConfiguration authenticationConfiguration() {
        return new AuthenticationConfiguration();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request.requestMatchers(
                        "/fanLetter/write", "/fanLetter/modify/**",
                        "/fanLetter/remove/**",
                        "/market/buy/write", "/market/buy/modify/**",
                        "/market/buy/comment/write", "/market/buy/comment/remove/**",
                        "/market/sell/write"
                ).hasRole("USER")
                        .anyRequest().permitAll())

                .addFilterBefore(new JwtFilter(customUserDetailsService, jwtTokenParser, jwtTokenService), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtTokenSetFilter(), UsernamePasswordAuthenticationFilter.class)
                // OAuth2
                .addFilterBefore(new CustomOAuth2AuthorizationCodeGrantFilter(clientRegistrationRepository, oAuth2AuthorizedClientRepository, authenticationManager(authenticationConfiguration()), customDefaultOAuth2UserService, snsInfo), OAuth2LoginAuthenticationFilter.class)
                .addFilterAfter(new OAuth2JwtTokenFilter(webService, jwtTokenService, jwtTokenParser, memberJoinService, snsInfo, kakaoJwk, googleJwk), OAuth2LoginAuthenticationFilter.class)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler()))

                .oauth2Login(oauth -> oauth.loginPage("/loginForm")
                        .authorizationEndpoint(end -> end.authorizationRequestResolver(customOAuth2AuthorizationRequestResolver))
                        .userInfoEndpoint(userInfo -> userInfo.userService(customDefaultOAuth2UserService))
                        .successHandler(new CustomOAuth2SuccessHandler(jwtTokenService, memberJoinService)))

                .logout(logout -> logout.logoutSuccessUrl("/").permitAll())
                .build();
    }

    @Bean
    public WebSecurityCustomizer configure() throws Exception {
        return (web) -> web
                .ignoring()
                .requestMatchers("/static/**", "/favicon.ico");
    }


}
