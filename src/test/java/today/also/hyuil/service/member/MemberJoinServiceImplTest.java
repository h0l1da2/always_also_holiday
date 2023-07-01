package today.also.hyuil.service.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import today.also.hyuil.domain.member.*;
import today.also.hyuil.exception.MemberNotFoundException;
import today.also.hyuil.exception.NotValidException;
import today.also.hyuil.repository.member.MemberJpaRepository;
import today.also.hyuil.service.member.inter.MemberJoinService;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

// module 이 여러개여서 인텔리제이가 오류를 띄움
// given
// when
// then
@SpringBootTest
class MemberJoinServiceImplTest {

    @Autowired
    private MemberJoinService memberJoinService;

    @Autowired
    private MemberJpaRepository memberJpaRepository;
    private final String password = "password!123";
    @BeforeEach
    void clean() {
        memberJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("가입 성공 처리")
    void join() {
        // given
        Member member = getMember(password);

        // when
        Member findMember = memberJoinService.joinMember(member);

        // then
        assertThat(findMember).isNotNull();
        assertThat(findMember.getMemberId()).isEqualTo(member.getMemberId());
    }
    @Test
    @DisplayName("memberId 로 멤버를 가져올 때, role 도 가져오는지 검증")
    void findMyAccountMemberId_get_role() {
        // given
        Member member = getMember(password);
        member = memberJoinService.joinMember(member);
        // when
        Member findMember = memberJoinService.findMyAccountMemberId(member.getMemberId());
        // then
        assertThat(findMember.getRole()).isNotNull();
        assertThat(findMember.getRole().getName()).isNotNull();
        assertThat(findMember.getRole().getName()).isEqualTo(Name.ROLE_USER);
    }
    @Test
    @DisplayName("id 로 멤버를 가져올 때, role 도 가져오는지 검증")
    void findMyAccount_get_role() {
        // given
        Member member = getMember(password);
        member = memberJoinService.joinMember(member);
        // when
        Member findMember = memberJoinService.findMyAccount(member.getId());
        // then
        assertThat(findMember.getRole()).isNotNull();
        assertThat(findMember.getRole().getName()).isNotNull();
        assertThat(findMember.getRole().getName()).isEqualTo(Name.ROLE_USER);
    }
    @Test
    @DisplayName("멤버가 가입이 안 되어 있을 때, idPwdValid 를 실행하면 MemberNotFoundException 발생")
    void id_pwd_valid_fail_memberNotFoundException() {
        // given
        Member member = getMember(password);

        // expected
        assertThrows(MemberNotFoundException.class,
                () -> memberJoinService.idPwdValid(member.getMemberId(), member.getPassword()));
    }

    @Test
    @DisplayName("아이디와 패스워드가 맞을 때 true 를 return 하는지 검증")
    void id_pwd_valid_success() throws MemberNotFoundException {
        // given
        Member member = getMember(password);
        member = memberJoinService.joinMember(member);

        // when
        boolean result = memberJoinService.idPwdValid(member.getMemberId(), password);

        // then
        assertThat(result).isTrue();
    }
    @Test
    @DisplayName("아이디와 패스워드가 틀릴 때 false 를 return 하는지 검증")
    void id_pwd_valid_fail() throws MemberNotFoundException {
        // given
        Member member = getMember(password);
        member = memberJoinService.joinMember(member);

        // when
        boolean result = memberJoinService.idPwdValid(member.getMemberId(), password+"123");

        // then
        assertThat(result).isFalse();
    }
    @Test
    @DisplayName("패스워드 변경시 성공 처리")
    void pw_update_success() throws NotValidException, MemberNotFoundException {
        // given -> Nullable 한 컬럼은 굳이,,,
        Member member = getMember(password);
        member = memberJoinService.joinMember(member);

        // when -> Pw change
        String newPwd = "Pwd123SSdsa";
        memberJoinService.passwordChange(member.getId(), "password!123", newPwd);
        Member findMember = memberJpaRepository.findById(member.getId()).orElse(null);
        // then -> Assertion
        assertThat(findMember).isNotNull();
        assertThat(findMember.getPassword()).isEqualTo(newPwd);
    }
    @Test
    @DisplayName("패스워드 변경시 실패 처리")
    void pw_update_fail() throws NotValidException, MemberNotFoundException {
        // given
        Member member = getMember(password);
        Member findMember = memberJoinService.joinMember(member);

        // when -> Pw change
        String newPwd = "Pwd123SSdsa";
        // 가입한 것과 다른 패스워드 줌
        // then -> Assertion
        assertThrows(NotValidException.class, () ->
                memberJoinService.passwordChange(findMember.getId(), "passwor2", newPwd));
    }

    private Member getMember(String password) {
        Address address = new Address();
        Member member = Member.builder()
                .memberId("memberId")
                .password(password)
                .name("name")
                .nickname("nickname")
                .email("email@email.com")
                .phone("01033332222")
                .address(address)
                .role(new Role(Name.ROLE_USER))
                .joinDate(new Date())
                .pwdModifyDate(new Date())
                .sns(Sns.NONE)
                .build();
        return member;
    }

}