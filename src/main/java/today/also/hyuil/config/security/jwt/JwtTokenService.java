package today.also.hyuil.config.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenRepository jwtTokenRepository;

    public Map<TokenName, String> getTokens(String memberId, Name role) {
        log.info("토큰 생성 = {}", memberId);

        String refreshToken = jwtTokenProvider.createRefreshToken();
        String accessToken = jwtTokenProvider.createAccessToken(
                memberId, getAuthorities(String.valueOf(role)));

        Map<TokenName, String> map = new HashMap<>();
        map.put(TokenName.ACCESS_TOKEN, accessToken);
        map.put(TokenName.REFRESH_TOKEN, refreshToken);
        return map;
    }

    public Map<TokenName, String> getReCreateTokens(String token) {
        log.info("토큰 재생성");
        String refreshT = jwtTokenProvider.createRefreshToken();
        String accessT = jwtTokenProvider.reCreateJwtToken(token);

        Map<TokenName, String> map = new HashMap<>();
        map.put(TokenName.ACCESS_TOKEN, accessT);
        map.put(TokenName.REFRESH_TOKEN, refreshT);
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
