package today.also.hyuil.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import today.also.hyuil.domain.member.Member;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class CustomUserDetails implements UserDetails, OAuth2User {

    private final Member member;
    private Map<String, Object> attributes;

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    public CustomUserDetails(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(
                new SimpleGrantedAuthority(
                        String.valueOf(member.getRole().getName())
                ));
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getMemberId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return member.getPwdModifyDate().after(new Date());
    }

    @Override
    public boolean isAccountNonLocked() {
        return member.getStopDate() == null ? true : false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return member.getRemoveDate() == null ? true : false;
    }

    @Override
    public String getName() {
        return member.getMemberId();
    }

    public String getNickName() {
        return member.getNickname();
    }

    public Long getId() {
        return member.getId();
    }
}
