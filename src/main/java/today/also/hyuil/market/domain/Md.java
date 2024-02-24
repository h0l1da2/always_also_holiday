package today.also.hyuil.market.domain;

import lombok.Getter;
import today.also.hyuil.market.dto.MarketWriteDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

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

    public Md(MarketWriteDto marketWriteDto) {
        this.price = marketWriteDto.getPrice();
        this.name = marketWriteDto.getName();
        this.quantity = marketWriteDto.getQuantity();
    }

    public void modifyMd(Long price, Long quantity, String name) {
        this.price = price;
        this.quantity = quantity;
        this.name = name;
    }
}
