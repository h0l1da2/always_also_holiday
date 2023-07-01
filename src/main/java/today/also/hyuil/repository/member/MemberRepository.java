package today.also.hyuil.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import today.also.hyuil.domain.member.Member;

import java.util.Optional;

import static today.also.hyuil.domain.member.QMember.member;

@Transactional
@Repository
public class MemberRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;


    public MemberRepository(EntityManager em, JPAQueryFactory query) {
        this.em = em;
        this.query = query;
    }

    public Member findByMemberIdRole(String memberId) {
        Optional<Member> findMember =
                query
                .select(member)
                .from(member)
                .where(member.memberId.eq(memberId))
                .stream().findFirst();
        Member member = findMember.orElse(null);
        if (findMember.isPresent()) {
            member.getRole().getName();
        }
        return member;
    }

    public Member findByIdRole(Long id) {
        Member member = em.find(Member.class, id);
        member.getRole().getName();
        return member;
    }

    public Member findById(Long id) {
        Member member = em.find(Member.class, id);
        em.close();
        return member;
    }

}
