package today.also.hyuil.domain.market;

import lombok.Getter;
import today.also.hyuil.domain.dto.market.MarketWriteDto;
import today.also.hyuil.domain.member.Member;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
public class MarketSell {

    @Id @GeneratedValue
    private Long id;
    private Long view;
    private String title;
    private String content;
    @Enumerated(EnumType.STRING)
    private Trade trade;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Date uploadDate;
    private Date updateDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "md_id")
    private Md md;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_sell_remover_id")
    private MarketSellRemover marketSellRemover;

    public MarketSell() {
    }

    public MarketSell(Status status, MarketWriteDto marketWriteDto, Member member, Md md) {
        this.title = marketWriteDto.getTitle();
        this.content = marketWriteDto.getContent();
        this.status = status;
        this.uploadDate = new Date();
        this.updateDate = new Date();
        this.md = md;
        this.member = member;
        this.trade = marketWriteDto.getTrade();
        this.view = 0L;
    }
    public MarketSell(Status status, MarketWriteDto marketWriteDto, Md md) {
        this.title = marketWriteDto.getTitle();
        this.content = marketWriteDto.getContent();
        this.status = status;
        this.uploadDate = new Date();
        this.updateDate = new Date();
        this.md = md;
        this.trade = marketWriteDto.getTrade();
        this.view = 0L;
    }

    public void updateViewCnt() {
        this.view++;
    }

    public void changeToBuyWriteDto(MarketWriteDto marketWriteDto) {
        this.title = marketWriteDto.getTitle();
        this.content = marketWriteDto.getContent();
        this.updateDate = new Date();
        this.md.modifyMd(marketWriteDto.getPrice(), marketWriteDto.getQuantity(), marketWriteDto.getName());
        this.trade = marketWriteDto.getTrade();
    }

    public void updateNewMarket(MarketSell market) {
        this.title = market.getTitle();
        this.content = market.getContent();
        this.updateDate = new Date();
        this.md.modifyMd(market.md.getPrice(), market.md.getQuantity(), market.md.getName());
        this.trade = market.getTrade();
    }

    public void itRemove(MarketSellRemover marketSellRemover) {
        this.marketSellRemover = marketSellRemover;
    }

    public void iAmSeller(Member member) {
        this.member = member;
    }
}
