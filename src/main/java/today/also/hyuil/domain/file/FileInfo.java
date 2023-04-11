package today.also.hyuil.domain.file;

import lombok.Getter;
import today.also.hyuil.domain.fanLetter.FanBoard;
import today.also.hyuil.domain.market.Market;

import javax.persistence.*;

@Entity
@Getter
public class FileInfo {

    @Id @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private IsWhere isWhere;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id")
    private File file;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fan_board_id")
    private FanBoard fanBoard;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_id")
    private Market market;

    public FileInfo() {}

    public FileInfo(File file) {
        this.file = file;
    }

    public void whereFileIs(IsWhere isWhere) {
        this.isWhere = isWhere;
    }

    public void fanBoardFile(FanBoard fanBoard) {
        this.fanBoard = fanBoard;
    }
}
