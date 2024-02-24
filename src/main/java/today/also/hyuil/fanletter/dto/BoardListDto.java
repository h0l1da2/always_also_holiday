package today.also.hyuil.fanletter.dto;

import lombok.Data;
import today.also.hyuil.fanletter.domain.FanBoard;
import today.also.hyuil.market.domain.Market;

import java.util.Date;

@Data
public class BoardListDto {

    private Long id;
    private String title;
    private String nickname;
    private Date uploadDate;
    private Long view;

    public BoardListDto() {
    }

    public BoardListDto(FanBoard fanBoard) {
        this.id = fanBoard.getId();
        this.title = fanBoard.getTitle();
        this.nickname = fanBoard.getMember().getNickname();
        this.uploadDate = fanBoard.getUploadDate();
        this.view = fanBoard.getView();
    }

    public BoardListDto(Market market) {
        this.id = market.getId();
        this.title = market.getTitle();
        this.nickname = market.getMember().getNickname();
        this.uploadDate = market.getUploadDate();
        this.view = market.getView();
    }
}
