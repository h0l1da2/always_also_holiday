package today.also.hyuil.config.security.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import today.also.hyuil.domain.member.Name;
import today.also.hyuil.domain.security.Token;
import today.also.hyuil.repository.security.JwtTokenRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *  - 토큰을 DB에 저장
 *  - 토큰을 반환
 *  - 토큰을 검증
 */
@Service
public class JwtTokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenRepository jwtTokenRepository;

    public JwtTokenService(JwtTokenProvider jwtTokenProvider, JwtTokenRepository jwtTokenRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtTokenRepository = jwtTokenRepository;
    }

    public Map<String, String> getTokens(String memberId, Name role) {
        System.out.println("여기까진 가나");
        String refreshToken = jwtTokenProvider.createRefreshToken();
        String accessToken = jwtTokenProvider.createAccessToken(
                memberId, getAuthorities(String.valueOf(role)));

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("refreshToken", refreshToken);
        return map;
    }

    public Map<String, String> getReCreateTokens(String token) {

        String refreshT = jwtTokenProvider.createRefreshToken();
        String accessT = jwtTokenProvider.reCreateJwtToken(token);

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", accessT);
        map.put("refreshToken", refreshT);
        return map;
    }

    public Token saveRefreshInDB(Token refreshToken) {
        // 기존 토큰이 있으면 바꿔치기, 없으면 새로 insert
        Token findToken = jwtTokenRepository.findByMemberId(refreshToken.getMemberId());
        if (findToken == null) {
            return jwtTokenRepository.insertRefreshToken(refreshToken);
        }
        findToken.changeToken(refreshToken);
        return jwtTokenRepository.updateNewToken(findToken);
    }

    public void saveRefreshToken(String memberId, String refreshToken) {
        Token token = duplicationTokenDB(memberId);

        if (token != null) {
            setNewToken(new Token(token.getId(), memberId, refreshToken));
        } else {
            saveRefreshInDB(new Token(memberId, refreshToken));
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(
                new SimpleGrantedAuthority(role));
        return authorities;
    }

    private Token duplicationTokenDB(String memberId) {
        return jwtTokenRepository.findByMemberId(memberId);
    }

    private Token setNewToken(Token token) {
        return jwtTokenRepository.updateNewToken(token);
    }

}
