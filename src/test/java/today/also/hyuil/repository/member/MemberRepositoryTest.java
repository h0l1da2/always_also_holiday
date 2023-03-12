package today.also.hyuil.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.domain.member.Name;

import javax.persistence.EntityManager;

@SpringBootTest
class MemberRepositoryTest {
    private EntityManager em;
    private JPAQueryFactory query;

    @Test
    void findByMemberIdRole() {
        MemberRepository memberRepository =
                new MemberRepository(em, query);
        Member member = memberRepository.findByMemberId("aaaa1");
        Assertions.assertThat(
                member.getRole().getName())
                .isEqualTo(Name.USER);


    }

}