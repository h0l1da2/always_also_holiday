package today.also.hyuil.domain.dto.fanLetter;

import lombok.Data;
import today.also.hyuil.domain.fanLetter.FanBoard;

import java.util.Date;

@Data
public class FanLetterViewDto {

    private String nickname;
    private String title;
    private String content;
    private Date uploadDate;
    public FanLetterViewDto() {

    }

    public FanLetterViewDto(FanBoard letter) {
        this.nickname = letter.getMember().getNickname();
        this.title = letter.getTitle();
        this.content = letter.getContent();
        this.uploadDate = letter.getUploadDate();
    }
}
