package today.also.hyuil.domain.member;

import lombok.Getter;
import today.also.hyuil.domain.dto.member.MemberJoinDto;

import javax.persistence.*;

@Entity
@Getter
public class Role {

    @Id @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private Name name;

    protected Role() {}

    public Role(MemberJoinDto memberJoinDto) {
        this.name = memberJoinDto.getRoleName();
    }

}
