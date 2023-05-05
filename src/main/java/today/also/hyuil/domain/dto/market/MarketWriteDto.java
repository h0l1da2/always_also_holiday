package today.also.hyuil.domain.dto.market;

import lombok.Data;
import today.also.hyuil.domain.market.Trade;

@Data
public class MarketWriteDto {

    private String title;
    private String name;
    private Long price;
    private Long quantity;
    private Trade trade;
    private String content;
}
