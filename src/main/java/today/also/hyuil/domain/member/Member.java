package today.also.hyuil.domain.member;

import lombok.Builder;
import lombok.Getter;
import today.also.hyuil.domain.dto.member.LoginDto;
import today.also.hyuil.domain.dto.member.MemberJoinDto;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Date;

@Entity
@Getter
@Builder
public class Member {

    @Id @GeneratedValue
    private Long id;
    private String memberId;
    private String password;
    private String name;
    private String nickname;

    @Email
    private String email;
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;
    private Date removeDate;
    private Date stopDate;
    private Date lastLogin;
    private Date joinDate;
    private Date pwdModifyDate;
    private String whyStop;
    @Enumerated(EnumType.STRING)
    private Sns sns;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "stop_admin")
    private Admin admin;

    protected Member() {}

    public Member(MemberJoinDto memberJoinDto, Address address, Role role) {
        this.memberId = memberJoinDto.getMemberId();
        this.password = memberJoinDto.getPassword();
        this.name = memberJoinDto.getName();
        this.nickname = memberJoinDto.getNickname();
        this.email = memberJoinDto.getEmail();
        this.phone = memberJoinDto.getPhone();
        this.address = address;
        this.role = role;
        this.sns = memberJoinDto.getSns();
        this.lastLogin = new Date();
        this.joinDate = new Date();
        this.pwdModifyDate = new Date();
    }

    public Member(LoginDto loginDto) {
        this.memberId = loginDto.getMemberId();
        this.password = loginDto.getPassword();
    }

    public Member(Long id, String memberId, String password, String name, String nickname, String email, String phone, Address address, Role role, Date removeDate, Date stopDate, Date lastLogin, Date joinDate, Date pwdModifyDate, String whyStop, Sns sns, Admin admin) {
        this.id = id;
        this.memberId = memberId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.removeDate = removeDate;
        this.stopDate = stopDate;
        this.lastLogin = lastLogin;
        this.joinDate = joinDate;
        this.pwdModifyDate = pwdModifyDate;
        this.whyStop = whyStop;
        this.sns = sns;
        this.admin = admin;
    }

    public void encodePassword(String password) {
        this.password = password;
    }
}
