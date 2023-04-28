package today.also.hyuil.domain.dto.fanLetter;

import lombok.Data;

@Data
public class FanCommentWriteDto {

    private Long letterNum;
    // 부모 댓글 번호
    private Long commentNum;
    private String content;
}
