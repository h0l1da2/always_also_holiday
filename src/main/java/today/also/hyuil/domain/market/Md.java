package today.also.hyuil.domain.market;

import lombok.Getter;
import today.also.hyuil.domain.dto.market.buy.BuyWriteDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Md {

    @Id @GeneratedValue
    private Long id;
    private Long price;
    private String name;
    private Long quantity;

    public Md() {

    }

    public Md(BuyWriteDto buyWriteDto) {
        this.price = buyWriteDto.getPrice();
        this.name = buyWriteDto.getName();
        this.quantity = buyWriteDto.getQuantity();
    }

    public void modifyMd(Long price, Long quantity, String name) {
        this.price = price;
        this.quantity = quantity;
        this.name = name;
    }
}
