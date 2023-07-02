package today.also.hyuil.config.security.auth.filter;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import today.also.hyuil.config.security.auth.CustomDefaultOAuth2UserService;
import today.also.hyuil.config.security.auth.jwk.GoogleJwk;
import today.also.hyuil.config.security.auth.jwk.KakaoJwk;
import today.also.hyuil.config.security.auth.tokenresponse.NaverProfileApiResponse;
import today.also.hyuil.config.security.auth.tokenresponse.TokenResponse;
import today.also.hyuil.config.security.auth.userinfo.SnsInfo;
import today.also.hyuil.config.security.jwt.JwtTokenParser;
import today.also.hyuil.config.security.jwt.JwtTokenService;
import today.also.hyuil.config.security.jwt.TokenName;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.domain.member.Sns;
import today.also.hyuil.service.member.inter.MemberJoinService;
import today.also.hyuil.service.web.WebService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class OAuth2JwtTokenFilter extends OncePerRequestFilter {

    private final WebService webService;
    private final JwtTokenService jwtTokenService;
    private final JwtTokenParser jwtTokenParser;
    private final MemberJoinService memberJoinService;
    private final SnsInfo snsInfo;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final CustomDefaultOAuth2UserService customDefaultOAuth2UserService;

    private final KakaoJwk kakaoJwk;
    private final GoogleJwk googleJwk;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("토큰 생성/저장 필터");
        TokenResponse tokenResponse =
                (TokenResponse) request.getAttribute("tokenResponse");
        String sns = String.valueOf(request.getAttribute("sns"));

        String memberId = "";
        String token = "";
        String sub = "";

        if (tokenResponse != null && StringUtils.hasText(sns)) {
            /**
             * - 토큰 파싱을 위해서는 발급자(--sns) 가 필요
             * iss : sns (발급자)
             * sub : 식별자
             * KAKAO : nickname , email
             * NAVER : email , name
             * GOOGLE : email , name
             */

            // 네이버
            if (sns.equals(Sns.NAVER.name())) {
                // 헤더 세팅
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer "+tokenResponse.getAccessToken());
                HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(headers);

                // https://openapi.naver.com/v1/nid/me 으로 프로필 정보 요청 보내기
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<NaverProfileApiResponse> responseEntity =
                        restTemplate.exchange(snsInfo.getNaverProfileApi(), HttpMethod.GET,
                                entity, NaverProfileApiResponse.class);
                NaverProfileApiResponse profileResponse = responseEntity.getBody();
                sub = profileResponse.getResponse().getId();

            }

            // KAKAO 일 경우 작업
            if (sns.equals(Sns.KAKAO.name()) | sns.equals(Sns.GOOGLE.name())) {

                token = tokenResponse.getIdToken();

                String kid = "";
                String n = "";
                String e = "";
                String kty = "";
                String alg = "";

                kid = jwtTokenParser.getTokenSecret(token, "header", "kid");

                // kid First인지 Second인지 확인하고, 해당 객체 쓰기
                if (sns.equals(Sns.KAKAO.name())) {
                    if (kid.equals(kakaoJwk.getKidFirst())) {
                        kakaoJwk.jwkSetting("first");
                    } else if (kid.equals(kakaoJwk.getKidSecond())) {
                        kakaoJwk.jwkSetting("second");
                    } else {
                        log.info("error: 카카오 공개 키 안 맞음");
                    }

                    n = kakaoJwk.getN();
                    e = kakaoJwk.getE();
                    kty = kakaoJwk.getKty();
                    alg = kakaoJwk.getAlg();
                }

                if (sns.equals(Sns.GOOGLE.name())) {
                    if (kid.equals(googleJwk.getKidFirst())) {
                        googleJwk.jwkSetting("first");
                    } else if (kid.equals(googleJwk.getKidSecond())) {
                        googleJwk.jwkSetting("second");
                    } else {
                        log.info("error: 구글 공개 키 안 맞음");
                    }

                    n = googleJwk.getN();
                    e = googleJwk.getE();
                    kty = googleJwk.getKty();
                    alg = googleJwk.getAlg();
                }

                PublicKey publicKey = jwtTokenParser.getPublicKey(n, e, kty);
                jwtTokenParser.isSignatureValid(publicKey, alg, token);

                Claims claims = jwtTokenParser.getSnsClaims(token, publicKey);

                sub = claims.get("sub", String.class);

            }

            // 이 아래부터 공통 작업(토큰 만드는 작업)
            memberId = sns+sub;
            Member member = memberJoinService.findMyAccountMemberId(memberId);
            String accessToken = "";
            if (member != null) {
                Map<TokenName, String> tokens =
                        jwtTokenService.getTokens(memberId, member.getRole().getName());
                accessToken = tokens.get(TokenName.ACCESS_TOKEN);
                String refreshToken = tokens.get(TokenName.REFRESH_TOKEN);
                request.setAttribute(TokenName.ACCESS_TOKEN.name(), accessToken);
                jwtTokenService.saveRefreshToken(memberId, refreshToken);

            }

            webService.sessionSetMember(member, request);

            // authentication 생성 후, SpringContext에 저장하는 작업
            ClientRegistration clientRegistration =
                    clientRegistrationRepository.findByRegistrationId(sns.toLowerCase());
            OAuth2AccessToken oAuth2AccessToken = getOAuth2AccessToken(tokenResponse);

            OAuth2UserRequest oAuth2UserRequest = new OAuth2UserRequest(clientRegistration, oAuth2AccessToken);

            accessToken = tokenResponse.getAccessToken();
            Authentication authentication = getAuthentication(accessToken, oAuth2UserRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String redirectUri = "/loginForm?redirect="+request.getRequestURI()+"&token="+accessToken;
            response.sendRedirect(redirectUri);

        }
        filterChain.doFilter(request, response);


    }

    private OAuth2AccessToken getOAuth2AccessToken(TokenResponse tokenResponse) {
        String accessToken = tokenResponse.getAccessToken();
        long expiresIn = tokenResponse.getExpiresIn();
        Instant expiresAt = Instant.now().plusSeconds(expiresIn);
        return new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, accessToken, Instant.now(), expiresAt);
    }

    private Authentication getAuthentication(String accessToken, OAuth2UserRequest oAuth2UserRequest) {
        OAuth2User oAuth2User = customDefaultOAuth2UserService.loadUser(oAuth2UserRequest);
        return new UsernamePasswordAuthenticationToken(oAuth2User, accessToken, oAuth2User.getAuthorities());
    }

}
