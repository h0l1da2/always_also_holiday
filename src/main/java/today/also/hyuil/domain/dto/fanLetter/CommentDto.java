package today.also.hyuil.domain.dto.fanLetter;

import lombok.Data;
import today.also.hyuil.domain.fanLetter.Comment;

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
        this.content = comment.getContent();
        this.nickname = comment.getMember().getNickname();
        this.uploadDate = comment.getUploadDate();
    }
}
