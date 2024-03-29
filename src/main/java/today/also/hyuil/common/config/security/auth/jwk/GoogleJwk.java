package today.also.hyuil.common.config.security.auth.jwk;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.yml")
@Getter
public class GoogleJwk {

    @Value("${token.keys.google.kid.first}")
    private String kidFirst;
    @Value("${token.keys.google.kty.first}")
    private String ktyFirst;
    @Value("${token.keys.google.alg.first}")
    private String algFirst;
    @Value("${token.keys.google.use.first}")
    private String useFirst;
    @Value("${token.keys.google.n.first}")
    private String nFirst;
    @Value("${token.keys.google.e.first}")
    private String eFirst;

    @Value("${token.keys.google.kid.second}")
    private String kidSecond;
    @Value("${token.keys.google.kty.second}")
    private String ktySecond;
    @Value("${token.keys.google.alg.second}")
    private String algSecond;
    @Value("${token.keys.google.use.second}")
    private String useSecond;
    @Value("${token.keys.google.n.second}")
    private String nSecond;
    @Value("${token.keys.google.e.second}")
    private String eSecond;

    private String kid;
    private String kty;
    private String alg;
    private String use;
    private String n;
    private String e;

    public void jwkSetting(String seq) {

        if (seq.equals("first")) {
            this.kid = kidFirst;
            this.kty = ktyFirst;
            this.alg = algFirst;
            this.use = useFirst;
            this.n = nFirst;
            this.e = eFirst;
        }
        if (seq.equals("second")) {
            this.kid = kidSecond;
            this.kty = ktySecond;
            this.alg = algSecond;
            this.use = useSecond;
            this.n = nSecond;
            this.e = eSecond;
        }
    }

}
