package today.also.hyuil.config.security.jwt;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import today.also.hyuil.domain.security.Token;
import today.also.hyuil.repository.security.JwtTokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.SignatureException;
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

    public Claims getClaims(String token) throws MalformedJwtException {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return claims;
    }

    private String[] splitToken(String token) {
        String[] jwt = token.split("\\.");
        return jwt;
    }

    public PublicKey getPublicKey(String n, String e, String kty) {
        BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(n));
        BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(e));
        RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
        try {
            System.out.println("kty = ????? "+kty);
            KeyFactory factory = KeyFactory.getInstance(kty);
            return factory.generatePublic(spec);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("알고리즘을 못 찾아씀");
            throw new RuntimeException(ex);
        } catch (InvalidKeySpecException ex) {
            System.out.println("잘못 된 키 스펙");
            throw new RuntimeException(ex);
        }
    }

    public String getTokenSecret(String token, String where, String what) {
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

        //시그니쳐는 byte[] 배열로 받아와야함..
        if (x == 2) {
            byte[] signatureBytes = Base64.getDecoder().decode(jwt[2]);
            return new String(signatureBytes, StandardCharsets.UTF_8);
        }

        byte[] tokenBytes = Base64.getDecoder().decode(jwt[x]);

        String jsonString = new String(tokenBytes, StandardCharsets.UTF_8);
        JsonObject headerJson = new JsonParser().parse(jsonString).getAsJsonObject();

        return headerJson.get(what).getAsString();
    }

    public boolean isSignatureValid(PublicKey publicKey, String alg, String token) {
        String[] jwt = splitToken(token);

        try {
            Signature verifier = Signature.getInstance("SHA256withRSA");
            verifier.initVerify(publicKey);
            verifier.update((jwt[0]+"."+jwt[1]).getBytes(StandardCharsets.UTF_8));

            return verifier.verify(Base64.getUrlDecoder().decode(jwt[2]));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("알고리즘을 못 찾음");
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            System.out.println("키 스펙이 잘못 됨");
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            System.out.println("서명이 잘못 됨");
            throw new RuntimeException(e);
        }
    }

    public Claims getSnsClaims(String idToken, PublicKey publicKey) {

        return Jwts.parserBuilder()
                .setSigningKeyResolver(new SigningKeyResolverAdapter() {
                    @Override
                    public Key resolveSigningKey(JwsHeader header, Claims claims) {
                        return publicKey;
                    }
                })
                .build()
                .parseClaimsJws(idToken)
                .getBody();
    }

    public String tokenInMemberId(String token) throws MalformedJwtException {
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
        if (StringUtils.hasText(token)) {
            return token;
        }
        return null;
    }


    private String getRoleToString(Claims claims) {
        List<Map<String, String>> roles = (List<Map<String, String>>) claims.get("role");
        return roles.get(0).get("authority");
    }
}
