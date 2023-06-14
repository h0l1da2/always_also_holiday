package today.also.hyuil.domain.fanLetter;

import jakarta.persistence.*;
import lombok.Getter;
import today.also.hyuil.domain.member.Admin;
import today.also.hyuil.domain.member.Member;

import java.util.Date;

@Entity
@Getter
public class BoardRemover {

    @Id
    @GeneratedValue
    private Long id;
    private Date removeDate;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    public BoardRemover() {

    }

    public void memberRemove(Member member) {
        removeDate = new Date();
        this.member = member;
    }
    public void adminRemove(Admin admin) {
        removeDate = new Date();
        this.admin = admin;
    }
}
