package today.also.hyuil.market.dto;

import lombok.Data;
import today.also.hyuil.market.domain.Trade;

@Data
public class MarketWriteDto {

    private String title;
    private String name;
    private Long price;
    private Long quantity;
    private Trade trade;
    private String content;
}
