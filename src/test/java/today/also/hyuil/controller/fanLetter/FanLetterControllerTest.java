package today.also.hyuil.controller.fanLetter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import today.also.hyuil.common.config.security.jwt.JwtTokenService;
import today.also.hyuil.common.config.security.jwt.TokenName;
import today.also.hyuil.fanletter.dto.FanLetterWriteDto;
import today.also.hyuil.member.domain.*;
import today.also.hyuil.fanletter.service.FanLetterService;
import today.also.hyuil.member.domain.type.Name;
import today.also.hyuil.member.domain.type.Sns;
import today.also.hyuil.member.service.MemberJoinService;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FanLetterControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FanLetterService fanLetterService;
    @Autowired
    private MemberJoinService memberJoinService;
    @Autowired
    private JwtTokenService jwtTokenService;

    @Test
    void fanLetterList() {
    }

    @Test
    void fanLetter() {
    }

    @Test
    @DisplayName("팬레터 작성을 성공하는 테스트 (사진X)")
    void write_post() throws Exception {
        //@RequestPart(value = "image", required = false) List<MultipartFile> files,
        //@RequestPart(value = "fanLetterWriteDto") FanLetterWriteDto fanLetterWriteDto,
        //HttpServletRequest request

        // given
        // 멤버 가입
        Member member = getJoinMember();

        // fanLetter 내용 들어옴
        FanLetterWriteDto fanLetterWriteDto = new FanLetterWriteDto();
        fanLetterWriteDto.setTitle("제목은 강휴일 화이팅");
        fanLetterWriteDto.setContent("내용은 별 거 없어요");

        // 세션 id 셋팅
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("id", member.getId());

        Map<TokenName, String> tokens = jwtTokenService.getTokens(member.getId(), member.getRole().getName());
        String jwtToken = tokens.get(TokenName.ACCESS_TOKEN);

        MockMultipartFile metaData =
                new MockMultipartFile(
                        "metadata", "fanLetterDto", MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsString(fanLetterWriteDto).getBytes(StandardCharsets.UTF_8));
        // expected
        mockMvc.perform(
                multipart("/fanLetter/write")
                        .file(metaData)
                        .accept(MediaType.APPLICATION_JSON)
                        .session(session)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("WRITE_OK"))
                .andDo(print());
    }

    @Test
    void testWrite() {
    }

    @Test
    void modifyRequest() {
    }

    @Test
    void modify() {
    }

    @Test
    void testModify() {
    }

    @Test
    void deleteLetter() {
    }

    private Member getJoinMember() {
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
        member = memberJoinService.joinMember(member);
        return member;
    }
}