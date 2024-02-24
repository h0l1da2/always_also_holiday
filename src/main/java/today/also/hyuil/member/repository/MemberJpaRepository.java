package today.also.hyuil.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import today.also.hyuil.member.domain.Member;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(String memberId);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByPhone(String phone);

}
