package today.also.hyuil.controller.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import today.also.hyuil.domain.dto.member.PwdDto;
import today.also.hyuil.domain.member.*;
import today.also.hyuil.repository.member.MemberJpaRepository;
import today.also.hyuil.service.member.inter.MemberJoinService;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemberInfoControllerTest {

    @Autowired
    private ObjectMapper mapper; // 요청을 보낼 때 객체를 json 으로

    @Autowired
    private MockMvc mockMvc; // 컨트롤러에 요청을 보낼 때

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private MemberJoinService memberJoinService;

    private final String password = "password!123";

    @Test
    @DisplayName("password 변경 시 성공")
    void modifyPwd_success() throws Exception {
        // given
        Member member = getMember();
        member = memberJoinService.joinMember(member);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("id", member.getId());

        PwdDto pwdDto = new PwdDto(password, "newPwd!1234");
        // expected
        mockMvc.perform(
                put("/info/password")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(pwdDto))
        ).andExpect(status().is2xxSuccessful())
         .andExpect(jsonPath("$.data").value("OK"))
         .andDo(print());
    }
    @Test
    @DisplayName("password 변경 시 짧으면 BadRequest")
    void modifyPwd() throws Exception {
        // given
        Member member = getMember();
        member = memberJoinService.joinMember(member);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("id", member.getId());

        PwdDto pwdDto = new PwdDto(password, "newPw");
        // expected
        mockMvc.perform(
                put("/info/password")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(pwdDto))
        ).andExpect(status().isBadRequest())
         .andExpect(jsonPath("$.data").value("BAD_FORM"))
         .andDo(print());
    }

    private Member getMember() {
        Address address = new Address();
        Member member = Member.builder()
                .memberId("memberId")
                .password("password!123")
                .name("name")
                .nickname("nickname")
                .email("email@email.com")
                .phone("01033332222")
                .address(address)
                .role(new Role(Name.ROLE_USER))
                .joinDate(new Date()) // Date -> LocalDate or LocalDateTime
                .pwdModifyDate(new Date())
                .sns(Sns.NONE)
                .build();
        return member;
    }
}