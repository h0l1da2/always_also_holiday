package today.also.hyuil.file.domain;

import jakarta.persistence.*;
import lombok.Getter;
import today.also.hyuil.fanletter.domain.FanBoard;
import today.also.hyuil.market.domain.Market;
import today.also.hyuil.market.domain.MarketSell;

@Entity
@Getter
public class FileInfo {

    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private IsWhere isWhere;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id")
    private Files file;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fan_board_id")
    private FanBoard fanBoard;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_id")
    private Market market;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_sell_id")
    private MarketSell marketSell;

    public FileInfo() {}

    public FileInfo(Files file) {
        this.file = file;
    }

    public void whereFileIs(IsWhere isWhere) {
        this.isWhere = isWhere;
    }

    public void fanBoardFile(FanBoard fanBoard) {
        this.fanBoard = fanBoard;
        this.isWhere = IsWhere.FAN_BOARD;
    }
    public void marketFile(Market market) {
        this.market = market;
        this.isWhere = IsWhere.MARKET;
    }
    public void marketSellFile(MarketSell marketSell) {
        this.marketSell = marketSell;
        this.isWhere = IsWhere.MARKET_SELL;
    }
}
