package today.also.hyuil.domain.security;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
public class Token {

    @Id @GeneratedValue
    private Long id;
    private String memberId;
    private String token;
    private Date createDate;
    private Date updateDate;

    protected Token() {}

    public Token(String memberId, String token) {
        this.memberId = memberId;
        this.token = token;
        this.createDate = new Date();
        this.updateDate = new Date();
    }
    public Token(Long id, String memberId, String token) {
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
