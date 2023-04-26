package today.also.hyuil.domain.dto.fanLetter;

import lombok.Data;

@Data
public class FanLetterWriteDto {

    private String title;
    private String content;

    public FanLetterWriteDto() {

    }

    public FanLetterWriteDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

