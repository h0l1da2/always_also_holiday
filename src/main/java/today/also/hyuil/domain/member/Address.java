package today.also.hyuil.domain.member;

import lombok.Getter;
import today.also.hyuil.domain.dto.member.MemberJoinDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Address {

    @Id @GeneratedValue
    private Long id;
    private String address;
    private String extraAddress;
    private String postcode;
    private String detail;

    public Address() {}

    public Address(MemberJoinDto memberJoinDto) {
        this.address = memberJoinDto.getAddress();
        this.extraAddress = memberJoinDto.getExtraAddress();
        this.postcode = memberJoinDto.getPostcode();
        this.detail = memberJoinDto.getDetail();
    }
}
