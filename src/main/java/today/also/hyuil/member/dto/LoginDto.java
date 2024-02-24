package today.also.hyuil.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {

    @NotBlank
    private String memberId;
    @NotBlank
    private String password;
}
