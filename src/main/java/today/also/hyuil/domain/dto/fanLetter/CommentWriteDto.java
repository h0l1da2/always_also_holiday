package today.also.hyuil.domain.dto.fanLetter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentWriteDto {

    @NotNull
    private Long boardNum;
    // 부모 댓글 번호
    @NotNull
    private Long commentNum;
    @NotBlank
    private String content;
}
