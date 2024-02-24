package today.also.hyuil.fanletter.domain;

import jakarta.persistence.*;
import lombok.Getter;
import today.also.hyuil.fanletter.dto.CommentWriteDto;
import today.also.hyuil.member.domain.Member;

import java.util.Date;

@Entity
@Getter
public class Comment {

    @Id
    @GeneratedValue
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

    public void setCommentValues(Member member, ReplyType replyType, CommentWriteDto commentWriteDto, FanBoard fanBoard) {
        this.content = commentWriteDto.getContent();
        this.uploadDate = new Date();
        this.member = member;
        this.fanBoard = fanBoard;
        this.replyType = replyType;
        this.rootId = commentWriteDto.getCommentNum();
    }

    public void removeComment(CommentRemover commentRemover) {
        this.commentRemover = commentRemover;
    }

}
