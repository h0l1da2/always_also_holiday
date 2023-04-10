package today.also.hyuil.domain.fanLetter;

import lombok.Getter;
import today.also.hyuil.domain.file.FileInfo;
import today.also.hyuil.domain.member.Member;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
public class FanBoard {

    @Id @GeneratedValue
    private Long id;
    private String title;
    private String content;
    private Date uploadDate;
    private Date updateDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "file_info_id")
    private FileInfo fileInfo;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "board_remover_id")
    private BoardRemover boardRemover;
}
