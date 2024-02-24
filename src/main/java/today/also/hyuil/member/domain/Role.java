package today.also.hyuil.member.domain;

import lombok.Getter;
import today.also.hyuil.member.dto.MemberJoinDto;

import jakarta.persistence.*;
import today.also.hyuil.member.domain.type.Name;

@Entity
@Getter
public class Role {

    @Id @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private Name name;

    protected Role() {}
    public Role(Name name) {
        this.name = name;
    }

    public Role(MemberJoinDto memberJoinDto) {
        this.name = memberJoinDto.getRoleName();
    }

}
