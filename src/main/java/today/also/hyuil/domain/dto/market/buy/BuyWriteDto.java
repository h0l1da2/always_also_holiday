package today.also.hyuil.domain.dto.market.buy;

import lombok.Data;
import today.also.hyuil.domain.market.Trade;

@Data
public class BuyWriteDto {

    private String title;
    private String name;
    private Long price;
    private Long quantity;
    private Trade trade;
    private String content;
}
