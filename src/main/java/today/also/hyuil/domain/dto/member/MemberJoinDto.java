package today.also.hyuil.domain.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import today.also.hyuil.domain.member.Name;
import today.also.hyuil.domain.member.Sns;

import jakarta.validation.constraints.Email;

@Getter
@Setter
@ToString
public class MemberJoinDto {

    @NotBlank
    private String memberId;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String nickname;
    @NotBlank
    @Email
    private String email;
    private String code;
    @NotBlank
    private String phone;
    @NotBlank
    private String address;
    @NotBlank
    private String extraAddress;
    @NotBlank
    private String postcode;
    @NotBlank
    private String detail;
    private Name roleName;
    private Sns sns;

}
