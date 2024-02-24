package today.also.hyuil.member.domain;

import lombok.Getter;

import jakarta.persistence.*;

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
