package today.also.hyuil.fanletter.domain;

import jakarta.persistence.*;
import lombok.Getter;
import today.also.hyuil.fanletter.dto.FanLetterWriteDto;
import today.also.hyuil.member.domain.Member;

import java.util.Date;

@Getter
@Entity
public class FanBoard {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String content;
    private Date uploadDate;
    private Date updateDate;
    private Long view;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_remover_id")
    private BoardRemover boardRemover;

    public FanBoard() {}

    public FanBoard(FanLetterWriteDto fanLetterWriteDto) {
        this.title = fanLetterWriteDto.getTitle();
        this.content = fanLetterWriteDto.getContent();
        this.updateDate = new Date();
        this.uploadDate = new Date();
        this.view = 0L;
    }

    public void writeMember(Member member) {
        this.member = member;
    }

    public void modifyLetter(FanLetterWriteDto fanLetterWriteDto) {
        this.title = fanLetterWriteDto.getTitle();
        this.content = fanLetterWriteDto.getContent();
        this.updateDate = new Date();
    }

    public void deleteFanLetter(BoardRemover boardRemover) {
        this.boardRemover = boardRemover;
    }

    public void updateViewCnt() {
        view++;
    }
}
