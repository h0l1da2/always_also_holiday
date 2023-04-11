package today.also.hyuil.domain.market;

import lombok.Getter;

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
}
