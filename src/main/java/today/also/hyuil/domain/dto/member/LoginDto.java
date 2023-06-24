package today.also.hyuil.domain.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {

    @NotBlank
    private String memberId;
    @NotBlank
    private String password;
}
