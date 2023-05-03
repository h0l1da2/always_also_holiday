package today.also.hyuil.domain.dto.fanLetter;

import lombok.Data;
import today.also.hyuil.domain.fanLetter.FanBoard;

@Data
public class PrevNextDto {

    private Long id;
    private String title;

    protected PrevNextDto() {

    }

    public PrevNextDto(FanBoard fanBoard) {
        this.id = fanBoard.getId();
        this.title = fanBoard.getTitle();
    }
}
