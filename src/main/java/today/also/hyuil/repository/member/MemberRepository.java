package today.also.hyuil.repository.member;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.member.Member;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class MemberRepository {

    private final EntityManager em;

    public MemberRepository(EntityManager em) {
        this.em = em;
    }

    public Member insertMember(Member member) {
        em.persist(member);
        em.close();
        return member;
    }
}
