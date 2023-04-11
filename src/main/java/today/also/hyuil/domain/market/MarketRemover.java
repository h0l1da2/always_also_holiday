package today.also.hyuil.domain.market;

import lombok.Getter;
import today.also.hyuil.domain.member.Admin;
import today.also.hyuil.domain.member.Member;

import javax.persistence.*;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;
}
