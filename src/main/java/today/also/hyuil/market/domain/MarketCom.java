package today.also.hyuil.market.domain;

import lombok.Getter;
import today.also.hyuil.fanletter.dto.CommentWriteDto;
import today.also.hyuil.fanletter.domain.ReplyType;
import today.also.hyuil.member.domain.Member;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Getter
public class MarketCom {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "market_id")
    private Market market;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ReplyType replyType;
    private String content;
    private Date uploadDate;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_com_remover_id")
    private MarketComRemover marketComRemover;
    private Long rootId;

    public MarketCom() {}

    public void setCommentValues(Member member, ReplyType replyType, CommentWriteDto commentWriteDto, Market market) {
        this.content = commentWriteDto.getContent();
        this.uploadDate = new Date();
        this.member = member;
        this.market = market;
        this.replyType = replyType;
        this.rootId = commentWriteDto.getCommentNum();
    }

    public void itRemove(MarketComRemover remover) {
        this.marketComRemover = remover;
    }
}
