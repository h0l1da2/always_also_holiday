package today.also.hyuil.domain.dto.market.sell;

import lombok.Data;
import today.also.hyuil.domain.market.MarketSell;

import java.util.Date;

@Data
public class SellListDto {

    private Long id;
    private String title;
    private String nickname;
    private Date uploadDate;
    private Long view;

    public SellListDto() {
    }

    public SellListDto(MarketSell marketSell) {
        this.id = marketSell.getId();
        this.title = marketSell.getTitle();
        this.nickname = marketSell.getMember().getNickname();
        this.uploadDate = marketSell.getUploadDate();
        this.view = marketSell.getView();
    }
}
