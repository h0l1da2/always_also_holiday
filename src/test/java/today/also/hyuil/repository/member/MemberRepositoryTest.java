package today.also.hyuil.repository.member;

import org.junit.jupiter.api.Test;
import today.also.hyuil.service.member.MemberJoinServiceImpl;
import today.also.hyuil.service.member.inter.MemberJoinService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class MemberRepositoryTest {

    @Test
    void test() {
        MemberRepository memberRepository = mock(MemberRepository.class);
        MemberJoinService memberJoinService = new MemberJoinServiceImpl(memberRepository);

        assertThat(memberJoinService).isNotNull();

//        org.junit.jupiter.api.Assertions.assertThrows(
//                NullPointerException.class,
//                () -> member.getRole());
    }
}