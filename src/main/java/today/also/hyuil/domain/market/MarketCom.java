package today.also.hyuil.domain.market;

import lombok.Getter;
import today.also.hyuil.domain.fanLetter.CommentRemover;
import today.also.hyuil.domain.fanLetter.ReplyType;
import today.also.hyuil.domain.member.Member;

import javax.persistence.*;
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
    @JoinColumn(name = "comment_remover_id")
    private CommentRemover commentRemover;
    private Long rootId;

    public MarketCom() {}
}
