package today.also.hyuil.domain.dto.member;

import lombok.Data;

@Data
public class DoubleCheckDto {

    private String memberId;
    private String nickname;
    private String email;
    private String code;
    private String phone;

}
