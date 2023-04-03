package today.also.hyuil;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.Base64;

public class TokenTest {

    @Test
    void tokenParse() {
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        String[] jwtParts = jwt.split("\\.");
        String header = jwtParts[0];
        String payload = jwtParts[1];
        String signature = jwtParts[2];

        // JWT Header 파싱
        byte[] headerBytes = Base64.getDecoder().decode(header);
        String headerJson = new String(headerBytes);
        System.out.println("JWT Header: " + headerJson);

        // JWT Payload 파싱
        byte[] payloadBytes = Base64.getDecoder().decode(payload);
        String payloadJson = new String(payloadBytes);
        System.out.println("JWT Payload: " + payloadJson);

        // JWT Signature 파싱
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        System.out.println("JWT Signature: " + new String(signatureBytes));
    }

    @Test
    void jsonTest() throws JSONException {
        String json = "{\"keys\":[{\"kid\":\"3f96980381e451efad0d2ddd30e3d3\",\"kty\":\"RSA\",\"alg\":\"RS256\",\"use\":\"sig\",\"n\":\"q8zZ0b_MNaLd6Ny8wd4cjFomilLfFIZcmhNSc1ttx_oQdJJZt5CDHB8WWwPGBUDUyY8AmfglS9Y1qA0_fxxs-ZUWdt45jSbUxghKNYgEwSutfM5sROh3srm5TiLW4YfOvKytGW1r9TQEdLe98ork8-rNRYPybRI3SKoqpci1m1QOcvUg4xEYRvbZIWku24DNMSeheytKUz6Ni4kKOVkzfGN11rUj1IrlRR-LNA9V9ZYmeoywy3k066rD5TaZHor5bM5gIzt1B4FmUuFITpXKGQZS5Hn_Ck8Bgc8kLWGAU8TzmOzLeROosqKE0eZJ4ESLMImTb2XSEZuN1wFyL0VtJw\",\"e\":\"AQAB\"},{\"kid\":\"9f252dadd5f233f93d2fa528d12fea\",\"kty\":\"RSA\",\"alg\":\"RS256\",\"use\":\"sig\",\"n\":\"qGWf6RVzV2pM8YqJ6by5exoixIlTvdXDfYj2v7E6xkoYmesAjp_1IYL7rzhpUYqIkWX0P4wOwAsg-Ud8PcMHggfwUNPOcqgSk1hAIHr63zSlG8xatQb17q9LrWny2HWkUVEU30PxxHsLcuzmfhbRx8kOrNfJEirIuqSyWF_OBHeEgBgYjydd_c8vPo7IiH-pijZn4ZouPsEg7wtdIX3-0ZcXXDbFkaDaqClfqmVCLNBhg3DKYDQOoyWXrpFKUXUFuk2FTCqWaQJ0GniO4p_ppkYIf4zhlwUYfXZEhm8cBo6H2EgukntDbTgnoha8kNunTPekxWTDhE5wGAt6YpT4Yw\",\"e\":\"AQAB\"}]}";
        JSONObject jsonObject = new JSONObject(json);

        String kid = String.valueOf(jsonObject.get("kid"));
    }
}
