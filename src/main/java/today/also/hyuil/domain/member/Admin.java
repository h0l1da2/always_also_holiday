package today.also.hyuil.domain.member;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Admin {

    @Id @GeneratedValue
    private Long id;
    private String adminId;
    private String password;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;
}
