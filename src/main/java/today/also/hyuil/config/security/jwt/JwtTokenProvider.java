package today.also.hyuil.config.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@PropertySource("classpath:application.yml")
public class JwtTokenProvider {

    @Value("${token.valid.time}")
    private int tokenValidTime;
//    private final Key key;

    public JwtTokenProvider(@Value("${token.secret.key}") String secretKey) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
    }

}

