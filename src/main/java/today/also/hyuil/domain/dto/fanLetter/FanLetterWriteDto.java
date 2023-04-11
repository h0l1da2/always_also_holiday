package today.also.hyuil.domain.dto.fanLetter;

import lombok.Data;

import java.util.List;

@Data
public class FanLetterWriteDto {

    private String title;
    private String content;
    private String memberId;
    private List<ImageDto> images;
}
