package today.also.hyuil.market.dto.buy;

import lombok.Data;

import java.util.Date;

@Data
public class BuyListDto {
    private Long id;
    private String title;
    private String nickname;
    private Date uploadDate;
    private Long view;

    public BuyListDto() {

    }

    public BuyListDto(Long id, String title, String nickname, Date uploadDate, Long view) {
        this.id = id;
        this.title = title;
        this.nickname = nickname;
        this.uploadDate = uploadDate;
        this.view = view;
    }
}
