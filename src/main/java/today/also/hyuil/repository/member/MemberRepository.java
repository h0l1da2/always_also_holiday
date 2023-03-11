package today.also.hyuil.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import today.also.hyuil.domain.member.Member;

import javax.persistence.EntityManager;

import java.util.Optional;

import static today.also.hyuil.domain.member.QMember.member;

@Repository
public class MemberRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;


    public MemberRepository(EntityManager em, JPAQueryFactory query) {
        this.em = em;
        this.query = query;
    }

    public Member insertMember(Member member) {
        em.persist(member);
        em.close();
        return member;
    }

    public Member findByMemberId(String memberId) {
        Optional<Member> findMember = query.select(member)
                .from(member)
                .where(member.memberId.eq(memberId))
                .stream().findFirst();

        return findMember.orElse(null);
    }

}
