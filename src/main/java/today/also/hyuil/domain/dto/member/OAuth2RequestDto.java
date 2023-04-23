package today.also.hyuil.domain.dto.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OAuth2RequestDto {

    private String redirectUri;
    private String code;
    private String errorDescription;
    private String error;
}
