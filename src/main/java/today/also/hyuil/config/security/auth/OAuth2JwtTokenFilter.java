package today.also.hyuil.config.security.auth;

import io.jsonwebtoken.Claims;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import today.also.hyuil.config.security.auth.jwk.KakaoJwk;
import today.also.hyuil.config.security.auth.userinfo.CustomAccessTokenResponse;
import today.also.hyuil.config.security.auth.userinfo.SnsInfo;
import today.also.hyuil.config.security.jwt.JwtTokenParser;
import today.also.hyuil.config.security.jwt.JwtTokenService;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.domain.member.Sns;
import today.also.hyuil.repository.member.MemberRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.PublicKey;
import java.util.Map;

public class OAuth2JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final JwtTokenParser jwtTokenParser;
    private final MemberRepository memberRepository;
    private final SnsInfo snsInfo;

    private final KakaoJwk kakaoJwk;
    public OAuth2JwtTokenFilter(JwtTokenService jwtTokenService, JwtTokenParser jwtTokenParser, MemberRepository memberRepository, SnsInfo snsInfo, KakaoJwk kakaoJwk) {
        this.jwtTokenService = jwtTokenService;
        this.jwtTokenParser = jwtTokenParser;
        this.memberRepository = memberRepository;
        this.snsInfo = snsInfo;
        this.kakaoJwk = kakaoJwk;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        CustomAccessTokenResponse tokenResponse =
                (CustomAccessTokenResponse) request.getAttribute("tokenResponse");
        String sns = String.valueOf(request.getAttribute("sns"));

        if (tokenResponse != null && StringUtils.hasText(sns)) {
            /**
             * - 토큰 파싱을 위해서는 발급자(--sns) 가 필요
             * iss : sns (발급자)
             * sub : PK 확인
             * KAKAO : nickname , email
             * NAVER : email , name
             * GOOGLE : email , name
             */
            String idToken = tokenResponse.getIdToken();
            String kid = null;
            String n = null;
            String e = null;
            String kty = null;
            String alg = null;
            try {
                kid = jwtTokenParser.getTokenSecret(idToken, "header", "kid");
            } catch (JSONException jsonE) {
                jsonE.printStackTrace();
            }

            // 해당 sns를 가지고 해당 pk
            if (sns.equals(Sns.KAKAO.name())) {

                // kid First인지 Second인지 확인하고, 해당 객체 쓰기

                if (kid.equals(kakaoJwk.getKidFirst())) {
                    kakaoJwk.jwkSetting("first");
                } else if (kid.equals(kakaoJwk.getKidSecond())) {
                    kakaoJwk.jwkSetting("second");
                } else {
                    throw new RuntimeException("카카오 공개키가 수정된 거 같음");
                }
                n = kakaoJwk.getN();
                e = kakaoJwk.getE();
                kty = kakaoJwk.getKty();
                alg = kakaoJwk.getAlg();
            }

            System.out.println("이 아래부터 문제 - idToken = " + idToken);
            System.out.println("이 아래부터 문제 - kid = " + kid);

            PublicKey publicKey = jwtTokenParser.getPublicKey(n, e, kty);
            jwtTokenParser.isSignatureValid(publicKey, alg, idToken);

            Claims claims = jwtTokenParser.getSnsClaims(idToken, publicKey);
            System.out.println("파싱끝");

            String pk = claims.get("sub", String.class);
            String memberId = sns+pk;
            Member member = memberRepository.findByMemberIdRole(memberId);

            if (member != null) {

                Map<String, String> tokens =
                        jwtTokenService.getTokens(memberId, member.getRole().getName());
                String accessToken = tokens.get("accessToken");
                String refreshToken = tokens.get("refreshToken");

                jwtTokenService.saveRefreshToken(memberId, refreshToken);


                response.setHeader("Authorization", "Bearer "+accessToken);
                System.out.println("여기까지 옴?");
            }
            String redirectUri = request.getRequestURI();
            response.sendRedirect(redirectUri);
        }

        filterChain.doFilter(request, response);


    }
}
