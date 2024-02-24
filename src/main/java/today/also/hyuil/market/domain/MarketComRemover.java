package today.also.hyuil.market.domain;

import lombok.Getter;
import today.also.hyuil.member.domain.type.Who;
import today.also.hyuil.member.domain.Member;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Getter
public class MarketComRemover {

    @Id @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;
    private Date removeDate;
    @Enumerated(EnumType.STRING)
    private Who who;

    public MarketComRemover() {

    }

    public MarketComRemover(Member member, Who who) {
        this.member = member;
        this.removeDate = new Date();
        this.who = who;
    }
}
