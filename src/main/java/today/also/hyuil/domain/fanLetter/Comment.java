package today.also.hyuil.domain.fanLetter;

import lombok.Getter;
import today.also.hyuil.domain.member.Member;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
public class Comment {

    @Id @GeneratedValue
    private Long id;
    private String content;
    private Date uploadDate;
    private Date updateDate;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "comment_remover_id")
    private CommentRemover commentRemover;
}
