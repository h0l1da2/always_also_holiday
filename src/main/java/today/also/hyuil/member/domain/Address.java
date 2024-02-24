package today.also.hyuil.member.domain;

import lombok.Getter;
import today.also.hyuil.member.dto.MemberJoinDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

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
