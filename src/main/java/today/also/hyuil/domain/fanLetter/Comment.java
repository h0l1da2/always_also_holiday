package today.also.hyuil.domain.fanLetter;

import lombok.Getter;
import today.also.hyuil.domain.dto.fanLetter.FanCommentWriteDto;
import today.also.hyuil.domain.member.Member;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
public class Comment {

    @Id @GeneratedValue
    private Long id;
    private Long rootId;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ReplyType replyType;
    private String content;
    private Date uploadDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "fan_board_id")
    private FanBoard fanBoard;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_remover_id")
    private CommentRemover commentRemover;

    public Comment() {
    }

    public void setCommentValues(Member member, ReplyType replyType, FanCommentWriteDto fanCommentWriteDto, FanBoard fanBoard) {
        this.content = fanCommentWriteDto.getContent();
        this.uploadDate = new Date();
        this.member = member;
        this.fanBoard = fanBoard;
        this.replyType = replyType;
        this.rootId = fanCommentWriteDto.getCommentNum();
    }

}
