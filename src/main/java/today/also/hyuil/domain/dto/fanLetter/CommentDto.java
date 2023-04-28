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

    public CommentDto(String nickname, Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.nickname = nickname;
        this.uploadDate = comment.getUploadDate();
    }
}
