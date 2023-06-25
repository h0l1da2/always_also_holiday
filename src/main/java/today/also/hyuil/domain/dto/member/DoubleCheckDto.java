package today.also.hyuil.domain.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DoubleCheckDto {

    @NotBlank
    private String memberId;
    @NotBlank
    private String nickname;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String code;
    @NotBlank
    private String phone;

}
