package today.also.hyuil.market.dto;

import lombok.Data;
import today.also.hyuil.market.domain.Market;
import today.also.hyuil.market.domain.MarketSell;
import today.also.hyuil.market.domain.Trade;

import java.util.Date;

@Data
public class MarketViewDto {

    private Long view;
    private String title;
    private String status;
    private Date uploadDate;
    private String nickname;
    private String name;
    private Long price;
    private Long quantity;
    private String trade;
    private String content;

    public MarketViewDto() {
    }

    public MarketViewDto(Market market) {
        this.view = market.getView();
        this.content = market.getContent();
        this.title = market.getTitle();
        this.status = market.getStatus().toString();
        this.uploadDate = market.getUploadDate();
        this.nickname = market.getMember().getNickname();
        this.name = market.getMd().getName();
        this.price = market.getMd().getPrice();
        this.quantity = market.getMd().getQuantity();
        if (market.getTrade().equals(Trade.DELIVERY)) {
            this.trade = "택배";
        } else {
            this.trade = "직거래";
        }
    }
    public MarketViewDto(MarketSell market) {
        this.view = market.getView();
        this.content = market.getContent();
        this.title = market.getTitle();
        this.status = market.getStatus().toString();
        this.uploadDate = market.getUploadDate();
        this.nickname = market.getMember().getNickname();
        this.name = market.getMd().getName();
        this.price = market.getMd().getPrice();
        this.quantity = market.getMd().getQuantity();
        if (market.getTrade().equals(Trade.DELIVERY)) {
            this.trade = "택배";
        } else {
            this.trade = "직거래";
        }
    }
}
