package today.also.hyuil.domain.dto.fanLetter;

import lombok.Data;

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

    public FanLetterListDto(Long id, String title, String nickname, Date uploadDate, Long view) {
        this.id = id;
        this.title = title;
        this.nickname = nickname;
        this.uploadDate = uploadDate;
        this.view = view;
    }
}
