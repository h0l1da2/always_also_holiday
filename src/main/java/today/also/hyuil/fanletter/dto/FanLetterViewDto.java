package today.also.hyuil.fanletter.dto;

import lombok.Data;
import today.also.hyuil.fanletter.domain.FanBoard;

import java.util.Date;

@Data
public class FanLetterViewDto {

    private String nickname;
    private String title;
    private String content;
    private Date uploadDate;
    private Long view;
    private Long commentCnt;
    public FanLetterViewDto() {

    }

    public FanLetterViewDto(FanBoard letter, Long commentCnt) {
        this.nickname = letter.getMember().getNickname();
        this.title = letter.getTitle();
        this.content = letter.getContent();
        this.uploadDate = letter.getUploadDate();
        this.view = letter.getView();
        this.commentCnt = commentCnt;
    }
}
