package today.also.hyuil.repository.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import today.also.hyuil.domain.member.Member;
import today.also.hyuil.domain.member.Name;
import today.also.hyuil.service.member.MemberJoinService;
import today.also.hyuil.service.member.MemberJoinServiceImpl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MemberRepositoryTest {

    @Test
    void test() {
        MemberRepository memberRepository = mock(MemberRepository.class);
        MemberJoinService memberJoinService = new MemberJoinServiceImpl(memberRepository);

        assertThat(memberJoinService).isNotNull();

        Member member = memberJoinService.memberIdLogin("aaaa1");

        assertThat(member).isNotNull();

//        org.junit.jupiter.api.Assertions.assertThrows(
//                NullPointerException.class,
//                () -> member.getRole());
    }
}