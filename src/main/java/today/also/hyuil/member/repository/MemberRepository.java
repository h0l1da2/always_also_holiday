package today.also.hyuil.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import today.also.hyuil.member.domain.Member;

import java.util.Optional;

import static today.also.hyuil.member.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

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
        em.close();
        return member;
    }

}
