package today.also.hyuil.domain.member;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Address {

    @Id @GeneratedValue
    private Long id;
    private String address;
    private String autoRoadAddress;
    private String zonecode;
    private String detail;

}
