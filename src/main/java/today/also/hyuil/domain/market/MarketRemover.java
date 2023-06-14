package today.also.hyuil.domain.market;

import lombok.Getter;
import today.also.hyuil.domain.Who;
import today.also.hyuil.domain.member.Admin;
import today.also.hyuil.domain.member.Member;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Getter
public class MarketRemover {

    @Id @GeneratedValue
    private Long id;
    private Date removeDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @Enumerated(EnumType.STRING)
    private Who who;

    public MarketRemover() {
    }

    public MarketRemover(Member member, Who who) {
        this.removeDate = new Date();
        this.member = member;
        this.who = who;
    }
}
