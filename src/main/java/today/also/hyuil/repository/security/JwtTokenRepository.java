package today.also.hyuil.repository.security;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.security.Token;

@Transactional
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

    public Token findById(Long id) {
        Token token = em.find(Token.class, id);
        em.close();
        return token;
    }

    public Token updateNewToken(Token token) {
        Token original = em.find(Token.class, token.getId());
        original.changeToken(token);
        em.close();

        return original;
    }


}
