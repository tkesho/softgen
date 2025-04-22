package org.test.sotfgen.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.test.sotfgen.entity.UserEntity;

import java.util.ArrayList;
import java.util.Collection;

public class SecUser implements UserDetails {

    public SecUser(UserEntity user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.enabled = user.getActive();
        this.authorities = new ArrayList<>();
        if (user.getRoles() != null) {
            this.authorities.addAll(user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .toList());
        }
        if (user.getAuthorities() != null) {
            this.authorities.addAll(user.getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                    .toList());
        }
    }

    @Getter
    private final Integer id;
    @Getter
    private final String username;
    @Getter
    private final String password;
    private final boolean enabled;
    private final Collection<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
