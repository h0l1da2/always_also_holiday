package today.also.hyuil.domain.security;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Token {

    @Id @GeneratedValue
    private Long id;
    private String memberId;
    private String token;

    protected Token() {}

    public Token(String memberId, String token) {
        this.memberId = memberId;
        this.token = token;
    }

    public void updateRefreshToken(String newToken) {
        this.token = newToken;
    }
}
