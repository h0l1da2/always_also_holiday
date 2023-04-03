package today.also.hyuil.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import today.also.hyuil.domain.security.Token;
import today.also.hyuil.repository.security.JwtTokenRepository;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;

@Component
public class JwtTokenParser {

    private final JwtParser jwtParser;
    private final JwtTokenRepository jwtTokenRepository;
    private static final String BEARER = "Bearer ";


    public JwtTokenParser(@Value("${token.secret.key}") String secretKey, JwtTokenRepository jwtTokenRepository) {
        this.jwtTokenRepository = jwtTokenRepository;
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public boolean validToken(String token, boolean isRefreshToken) {
        Claims claims = getClaims(token);
        if (claims.getSubject() != null | !isRefreshToken) {
            return !claims.getExpiration().before(new Date());
        }
        return false;
    }

    public Token getRefreshToken(String accessToken) {
        Claims claims = getClaims(accessToken);
        if (claims.getSubject() != null) {
            String memberId = claims.get("memberId", String.class);
            Token refreshToken = jwtTokenRepository.findByMemberId(memberId);
            return refreshToken;
        }
        return null;
    }

    public Collection<GrantedAuthority> getAuthorities(String accessToken) {
        Claims claims = getClaims(accessToken);
        String role = getRoleToString(claims);

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));

        return authorities;
    }

    public Claims getClaims(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return claims;
    }

    private Key getKey(String publicKey) {
        byte[] keyBytes = publicKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String[] splitToken(String token) {
        String[] jwt = token.split("\\.");
        return jwt;
    }

    public PublicKey getPublicKey(String kid, String n, String e, String alg) throws InvalidKeySpecException, NoSuchAlgorithmException {
        BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(n));
        BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(e));

        RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory factory = KeyFactory.getInstance(alg);

        return factory.generatePublic(spec);
    }

    public String getTokenSecret(String token, String where, String what) throws JSONException {
        String[] jwt = splitToken(token);
        int x = 0;
        if (where.equals("header")) {
            x = 0;
        }
        if (where.equals("payload")) {
            x = 1;
        }
        if (where.equals("signature")) {
            x = 2;
        }

        //시그니쳐는 byte[] 배열로 받아와야함.. 나중에 수정합시다

        byte[] tokenBytes = Base64.getDecoder().decode(jwt[x]);

        String jsonString = new String(tokenBytes, StandardCharsets.UTF_8);
        JSONObject headerJson = new JSONObject(jsonString);

        return String.valueOf(headerJson.get(what));
    }

    public boolean isSignatureValid(PublicKey publicKey, String alg, String token) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        String[] jwt = splitToken(token);

        Signature verifier = Signature.getInstance(alg);
        verifier.initVerify(publicKey);
        verifier.update((jwt[0]+"."+jwt[1]).getBytes(StandardCharsets.UTF_8));

        return verifier.verify(jwt[2].getBytes());
    }

    public Claims getSnsClaims(String idToken, String publicKey) {
        Key key = getKey(publicKey);
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJwt(idToken)
                .getBody();
    }

    public String tokenInMemberId(String token) {
        Claims claims = getClaims(token);
        return String.valueOf(claims.get("memberId"));
    }

    public String getTokenHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.toLowerCase().startsWith(BEARER.toLowerCase())) {
            return bearerToken.substring(BEARER.length()).trim();
        }
        return null;
    }

    public String getTokenUrl(HttpServletRequest request) {
        String token = request.getParameter("token");
        System.out.println("token1 = " + token);
        if (StringUtils.hasText(token)) {
            System.out.println("token2 = " + token);
            return token;
        }
        return null;
    }


    private String getRoleToString(Claims claims) {
        List<Map<String, String>> roles = (List<Map<String, String>>) claims.get("role");
        return roles.get(0).get("authority");
    }
}
