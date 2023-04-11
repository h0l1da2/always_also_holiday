package today.also.hyuil.domain.market;

import lombok.Getter;
import today.also.hyuil.domain.member.Member;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
public class Market {

    @Id @GeneratedValue
    private Long id;
    private String title;
    private String content;
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
}
