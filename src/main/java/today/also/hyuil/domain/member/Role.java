package today.also.hyuil.domain.member;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Role {

    @Id @GeneratedValue
    private Long id;
    private Name name;
}
