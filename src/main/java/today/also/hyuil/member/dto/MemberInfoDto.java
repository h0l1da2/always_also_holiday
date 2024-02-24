package today.also.hyuil.member.dto;

import lombok.Data;
import today.also.hyuil.member.domain.Member;

@Data
public class MemberInfoDto {

    private String memberId;
    private String name;
    private String nickname;
    private String email;
    private String phone;

    public MemberInfoDto() {

    }
    public MemberInfoDto(Member member) {
        this.memberId = member.getMemberId();
        this.name = member.getName();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.phone = member.getPhone();
    }
}
