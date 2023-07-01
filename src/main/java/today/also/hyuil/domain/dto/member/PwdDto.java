package today.also.hyuil.domain.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PwdDto {

    @NotBlank
    private String password;
    @NotBlank
    private String newPwd;

    public PwdDto() {

    }

    public PwdDto(String password, String newPw) {
        this.password = password;
        this.newPwd = newPw;
    }
}
