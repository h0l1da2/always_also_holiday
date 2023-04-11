package today.also.hyuil.domain.dto.fanLetter;

import lombok.Data;

@Data
public class FanLetterWriteDto {

    private String title;
    private String content;
    private String memberId;
    // file
    private String fileName;
    // jpg png gif ...
    private String mimeType;
    private Long fileSize;
}
