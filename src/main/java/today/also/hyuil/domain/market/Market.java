package today.also.hyuil.domain.market;

import lombok.Getter;
import today.also.hyuil.domain.dto.market.buy.BuyWriteDto;
import today.also.hyuil.domain.member.Member;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
public class Market {

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
    @JoinColumn(name = "market_remover_id")
    private MarketRemover marketRemover;

    public Market() {
    }

    public Market(Status status, BuyWriteDto buyWriteDto, Member member, Md md) {
        this.title = buyWriteDto.getTitle();
        this.content = buyWriteDto.getContent();
        this.status = status;
        this.uploadDate = new Date();
        this.updateDate = new Date();
        this.md = md;
        this.member = member;
        this.trade = buyWriteDto.getTrade();
        this.view = 0L;
    }
}
