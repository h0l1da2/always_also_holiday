package today.also.hyuil.repository.security;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import today.also.hyuil.domain.security.Token;

import javax.persistence.EntityManager;
import java.util.Optional;

import static today.also.hyuil.domain.security.QToken.token1;

@Repository
public class JwtTokenRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public JwtTokenRepository(EntityManager em, JPAQueryFactory query) {
        this.em = em;
        this.query = query;
    }

    public Token insertRefreshToken(Token token) {
        em.persist(token);
        em.close();
        return token;
    }

    public Token findByMemberId(String memberId) {
        Optional<Token> token = query
                .select(token1)
                .from(token1)
                .where(token1.memberId.eq(memberId))
                .stream().findFirst();
        return token.orElse(null);
    }

    public void updateToken(String memberId, String newToken) {
        query.update(token1)
                .set(token1.token, newToken)
                .where(token1.memberId.eq(memberId))
                .execute();
    }


}
