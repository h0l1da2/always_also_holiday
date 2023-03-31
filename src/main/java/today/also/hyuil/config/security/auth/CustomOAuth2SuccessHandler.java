package today.also.hyuil.config.security.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import today.also.hyuil.config.security.CustomUserDetails;
import today.also.hyuil.config.security.jwt.JwtTokenService;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.repository.member.MemberRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 토큰 발급
 */
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenService jwtTokenService;
    private final MemberRepository memberRepository;

    public CustomOAuth2SuccessHandler(JwtTokenService jwtTokenService, MemberRepository memberRepository) {
        this.jwtTokenService = jwtTokenService;
        this.memberRepository = memberRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Member findMember = memberRepository.findByMemberIdRole(userDetails.getUsername());
        Map<String, String> tokens = jwtTokenService.getTokens(findMember.getMemberId(), findMember.getRole().getName());

        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        System.out.println("토큰을 반환함 : "+accessToken);
        System.out.println("토큰을 반환함 : "+refreshToken);
        jwtTokenService.saveRefreshToken(findMember.getMemberId(), refreshToken);
        System.out.println("리프레쉬 저장");
        response.setHeader("Authorization", "Bearer "+accessToken);
        System.out.println("헤더 저장");

        String redirectUrl = request.getParameter("redirectUrl");

//        if (redirectUrl != null) {
//            response.sendRedirect(redirectUrl);
//        } else {
//            response.sendRedirect("/");
//        }


    }

}
