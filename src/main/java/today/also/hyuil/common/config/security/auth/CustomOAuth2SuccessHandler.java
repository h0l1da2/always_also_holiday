package today.also.hyuil.common.config.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import today.also.hyuil.common.config.security.CustomUserDetails;
import today.also.hyuil.common.config.security.jwt.JwtTokenService;
import today.also.hyuil.common.config.security.jwt.TokenName;
import today.also.hyuil.member.domain.Member;
import today.also.hyuil.member.service.MemberJoinService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 토큰 발급
 */
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenService jwtTokenService;
    private final MemberJoinService memberJoinService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Member findMember = memberJoinService.findMyAccountMemberId(userDetails.getUsername());
        Map<TokenName, String> tokens = jwtTokenService.getTokens(findMember.getId(), findMember.getRole().getName());

        String accessToken = tokens.get(TokenName.ACCESS_TOKEN);
        String refreshToken = tokens.get(TokenName.REFRESH_TOKEN);

        jwtTokenService.saveRefreshToken(findMember.getId(), refreshToken);
        response.setHeader("Authorization", "Bearer "+accessToken);

        String redirectUrl = request.getParameter("redirectUrl");

//        if (redirectUrl != null) {
//            response.sendRedirect(redirectUrl);
//        } else {
//            response.sendRedirect("/");
//        }

    }

}
