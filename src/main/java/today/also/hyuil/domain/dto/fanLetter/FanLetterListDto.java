package today.also.hyuil.domain.dto.fanLetter;

import lombok.Data;
import today.also.hyuil.domain.fanLetter.FanBoard;

import java.util.Date;

@Data
public class FanLetterListDto {

    private Long id;
    private String title;
    private String nickname;
    private Date uploadDate;
    private Long view;

    public FanLetterListDto() {
    }

    public FanLetterListDto(FanBoard fanBoard) {
        this.id = fanBoard.getId();
        this.title = fanBoard.getTitle();
        this.nickname = fanBoard.getMember().getNickname();
        this.uploadDate = fanBoard.getUploadDate();
        this.view = fanBoard.getView();
    }
}
