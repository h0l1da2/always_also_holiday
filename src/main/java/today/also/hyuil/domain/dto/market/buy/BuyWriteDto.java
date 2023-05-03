package today.also.hyuil.domain.dto.market.buy;

import lombok.Data;

@Data
public class BuyWriteDto {

    private String title;
    private String name;
    private Long price;
    private Long quantity;
    private String howTrade;
    private String content;
}
