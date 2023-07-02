package today.also.hyuil.config.security.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import today.also.hyuil.config.security.CustomUserDetails;
import today.also.hyuil.config.security.jwt.JwtTokenService;
import today.also.hyuil.config.security.jwt.TokenName;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.service.member.inter.MemberJoinService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 토큰 발급
 */
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenService jwtTokenService;
    private final MemberJoinService memberJoinService;

    public CustomOAuth2SuccessHandler(JwtTokenService jwtTokenService, MemberJoinService memberJoinService) {
        this.jwtTokenService = jwtTokenService;
        this.memberJoinService = memberJoinService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Member findMember = memberJoinService.findMyAccountMemberId(userDetails.getUsername());
        Map<TokenName, String> tokens = jwtTokenService.getTokens(findMember.getMemberId(), findMember.getRole().getName());

        String accessToken = tokens.get(TokenName.ACCESS_TOKEN);
        String refreshToken = tokens.get(TokenName.REFRESH_TOKEN);

        jwtTokenService.saveRefreshToken(findMember.getMemberId(), refreshToken);
        response.setHeader("Authorization", "Bearer "+accessToken);

        String redirectUrl = request.getParameter("redirectUrl");

//        if (redirectUrl != null) {
//            response.sendRedirect(redirectUrl);
//        } else {
//            response.sendRedirect("/");
//        }


    }

}
