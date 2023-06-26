package today.also.hyuil.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import today.also.hyuil.domain.member.Member;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long id);
    Optional<Member> findByMemberId(String memberId);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByPhone(String phone);
    Member save(Member member);

}
