package today.also.hyuil.common.security.type;

import lombok.Getter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.Date;

@Entity
@Getter
public class Token {

    @Id @GeneratedValue
    private Long id;
    private Long memberId;
    private String token;
    private Date createDate;
    private Date updateDate;

    protected Token() {}

    public Token(Long memberId, String token) {
        this.memberId = memberId;
        this.token = token;
        this.createDate = new Date();
        this.updateDate = new Date();
    }
    public Token(Long id, Long memberId, String token) {
        this.id = id;
        this.memberId = memberId;
        this.token = token;
        this.createDate = new Date();
        this.updateDate = new Date();
    }

    public void updateRefreshToken(String newToken) {
        this.token = newToken;
        this.updateDate = new Date();
    }

    public void changeToken(Token token) {
        this.token = token.getToken();
        this.createDate = new Date();
        this.updateDate = new Date();
    }

}
