package today.also.hyuil.domain.dto.fanLetter;

import lombok.Data;

@Data
public class FanCommentWriteDto {

    private Long letterNum;
    private Long commentNum;
    private String content;
}
