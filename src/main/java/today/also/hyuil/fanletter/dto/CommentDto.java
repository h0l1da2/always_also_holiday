package today.also.hyuil.fanletter.dto;

import lombok.Data;
import today.also.hyuil.fanletter.domain.Comment;
import today.also.hyuil.market.domain.MarketCom;
import today.also.hyuil.market.domain.MarketSellCom;

import java.util.Date;

@Data
public class CommentDto {

    private Long id;
    private Long replyId;
    private String content;
    private String nickname;
    private Date uploadDate;

    public CommentDto() {

    }

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.replyId = comment.getRootId();
        this.content = comment.getContent();
        this.nickname = comment.getMember().getNickname();
        this.uploadDate = comment.getUploadDate();
    }
    public CommentDto(MarketCom comment) {
        this.id = comment.getId();
        this.replyId = comment.getRootId();
        this.content = comment.getContent();
        this.nickname = comment.getMember().getNickname();
        this.uploadDate = comment.getUploadDate();
    }
    public CommentDto(MarketSellCom comment) {
        this.id = comment.getId();
        this.replyId = comment.getRootId();
        this.content = comment.getContent();
        this.nickname = comment.getMember().getNickname();
        this.uploadDate = comment.getUploadDate();
    }

    public void itRemoved() {
        this.content = "삭제 된 댓글입니다";
        this.nickname = "";
        this.uploadDate = null;
    }
}
