package today.also.hyuil.service.member;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.config.security.jwt.JwtTokenProvider;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.domain.security.Token;
import today.also.hyuil.service.member.inter.MemberLoginService;

import java.util.HashMap;
import java.util.Map;

@Transactional
@Service
public class MemberLoginServiceImpl implements MemberLoginService {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberLoginServiceImpl(UserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean idPwdValid(Member member) {
        UserDetails userDetails = getUserDetails(member);
        return passwordEncoder.matches(member.getPassword(), userDetails.getPassword());
    }

    @Override
    public Map<String, String> getTokens(Member member) {
        UserDetails userDetails = getUserDetails(member);

        String refreshToken = jwtTokenProvider.createRefreshToken();
        String accessToken = jwtTokenProvider.createAccessToken(
                userDetails.getUsername(), userDetails.getAuthorities());

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("refreshToken", refreshToken);
        return map;
    }

    @Override
    public void saveRefreshToken(String memberId, String refreshToken) {
        Token token = jwtTokenProvider.duplicationTokenDB(memberId);

        if (token != null) {
            jwtTokenProvider.setNewToken(new Token(token.getId(), memberId, refreshToken));
        } else {
            jwtTokenProvider.saveRefreshInDB(new Token(memberId, refreshToken));
        }

    }

    private UserDetails getUserDetails(Member member) {
        return userDetailsService.loadUserByUsername(member.getMemberId());
    }


}

