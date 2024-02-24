package today.also.hyuil.common.config.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import today.also.hyuil.common.exception.BadRequestException;
import today.also.hyuil.common.exception.Error;
import today.also.hyuil.common.security.jwt.JwtTokenRepository;
import today.also.hyuil.common.security.type.Token;
import today.also.hyuil.member.domain.type.Name;

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
    private final JwtTokenParser jwtTokenParser;

    public String getToken(HttpServletRequest request) {
        String token = jwtTokenParser.getTokenHeader(request);
        if (!tokenNullCheck(token)) {
            String tokenUrl = jwtTokenParser.getTokenUrl(request);
            if (!tokenNullCheck(tokenUrl)) {
                log.error("인증 실패 : Token NULL");
                throw new BadRequestException(Error.ACCESS_DENIED);
            }
            // url 파라미터에 토큰이 있을 경우
            token = tokenUrl;
        }
        return token;
    }

    public Collection<GrantedAuthority> getAuthoritiesFromToken(String accessToken) {
        return jwtTokenParser.getAuthoritiesForService(accessToken);
    }


        public Map<String, String> reCreateTokens(String token) {
        Long id = jwtTokenParser.tokenInMemberId(token);

        Map<String, String> map = getReCreateTokens(token);
        map.put("id", id.toString());

        return map;
    }

    public void tokenValid(String token) {
        boolean tokenValid = jwtTokenParser.validToken(token, false);
        if (!tokenValid) {
            Token refreshToken = jwtTokenParser.getRefreshToken(token);
            if (refreshToken == null) {
                log.error("인증 실패 : 리프레쉬 토큰 NULL");
                throw new BadRequestException(Error.ACCESS_DENIED);
            }

            boolean refreshValid = jwtTokenParser.validToken(refreshToken.getToken(), true);
            if (!refreshValid) {
                log.error("인증 실패 : 리프레쉬 토큰 시간 만료");
                throw new BadRequestException(Error.ACCESS_DENIED);
            }
        }
    }

    public Map<TokenName, String> getTokens(Long id, Name role) {
        log.info("토큰 생성 = {}", id);

        String refreshToken = jwtTokenProvider.createRefreshToken();
        String accessToken = jwtTokenProvider.createAccessToken(
                id, getAuthoritiesFromRole(String.valueOf(role))
        );

        Map<TokenName, String> map = new HashMap<>();
        map.put(TokenName.ACCESS_TOKEN, accessToken);
        map.put(TokenName.REFRESH_TOKEN, refreshToken);
        return map;
    }

    private Map<String, String> getReCreateTokens(String token) {
        log.info("토큰 재생성");
        String refreshT = jwtTokenProvider.createRefreshToken();
        String accessT = jwtTokenProvider.reCreateJwtToken(token);

        Map<String, String> map = new HashMap<>();
        map.put(TokenName.ACCESS_TOKEN.name(), accessT);
        map.put(TokenName.REFRESH_TOKEN.name(), refreshT);
        return map;
    }

    public Token saveRefreshInDB(Token refreshToken) {
        // 기존 토큰이 있으면 바꿔치기, 없으면 새로 insert
        Token findToken = jwtTokenRepository.findById(refreshToken.getMemberId());
        if (findToken == null) {
            return jwtTokenRepository.insertRefreshToken(refreshToken);
        }
        findToken.changeToken(refreshToken);
        return jwtTokenRepository.updateNewToken(findToken);
    }

    public void saveRefreshToken(Long id, String refreshToken) {
        Token token = duplicationTokenDB(id);

        if (token != null) {
            setNewToken(new Token(token.getId(), id, refreshToken));
        } else {
            saveRefreshInDB(new Token(id, refreshToken));
        }
    }

    private Collection<? extends GrantedAuthority> getAuthoritiesFromRole(String role) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }

    private Token duplicationTokenDB(Long id) {
        return jwtTokenRepository.findById(id);
    }

    private Token setNewToken(Token token) {
        return jwtTokenRepository.updateNewToken(token);
    }
    private boolean tokenNullCheck(String token) {
        try {
            if (token == null) {
                return false;
            }
            if (token.equals("null")) {
                return false;
            }
            if (!StringUtils.hasText(token)) {
                return false;
            }
            return true;
        } catch (NullPointerException e) {
            log.error("토큰이 없음");
            return false;
        }
    }
}
