package today.also.hyuil.domain.dto.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import today.also.hyuil.domain.member.Name;
import today.also.hyuil.domain.member.Sns;

import javax.validation.constraints.Email;

@Getter
@Setter
@ToString
public class MemberJoinDto {

    private String memberId;
    private String password;
    private String name;
    private String nickname;
    @Email
    private String email;
    private String code;
    private String phone;
    private String address;
    private String extraAddress;
    private String postcode;
    private String detail;
    private Name roleName;
    private Sns sns;

}
