package today.also.hyuil.domain.dto.market;

import lombok.Data;
import today.also.hyuil.domain.market.Market;
import today.also.hyuil.domain.market.Status;
import today.also.hyuil.domain.market.Trade;

import java.util.Date;

@Data
public class MarketViewDto {

    private Long view;
    private String title;
    private Status status;
    private Date uploadDate;
    private String nickname;
    private String name;
    private Long price;
    private Long quantity;
    private Trade trade;

    public MarketViewDto() {
    }

    public MarketViewDto(Market market) {
        this.view = market.getView();
        this.title = market.getTitle();
        this.status = market.getStatus();
        this.uploadDate = market.getUploadDate();
        this.nickname = market.getMember().getNickname();
        this.name = market.getMd().getName();
        this.price = market.getMd().getPrice();
        this.quantity = market.getMd().getQuantity();
        this.trade = market.getTrade();

    }
}
