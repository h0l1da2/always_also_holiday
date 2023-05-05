package today.also.hyuil.domain.market;

import lombok.Getter;
import today.also.hyuil.domain.dto.fanLetter.CommentWriteDto;
import today.also.hyuil.domain.fanLetter.ReplyType;
import today.also.hyuil.domain.member.Member;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
public class MarketSellCom {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "market_sell_id")
    private MarketSell market;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ReplyType replyType;
    private String content;
    private Date uploadDate;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_sell_com_remover_id")
    private MarketSellComRemover marketSellComRemover;
    private Long rootId;

    public MarketSellCom() {}

    public void setCommentValues(Member member, ReplyType replyType, CommentWriteDto commentWriteDto, MarketSell market) {
        this.content = commentWriteDto.getContent();
        this.uploadDate = new Date();
        this.member = member;
        this.market = market;
        this.replyType = replyType;
        this.rootId = commentWriteDto.getCommentNum();
    }

    public void itRemove(MarketSellComRemover marketSellComRemover) {
        this.marketSellComRemover = marketSellComRemover;
    }
}
