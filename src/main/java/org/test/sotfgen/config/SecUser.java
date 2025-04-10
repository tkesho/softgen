package org.test.sotfgen.config;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.test.sotfgen.entity.AuthorityEntity;
import org.test.sotfgen.entity.UserEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SecUser implements UserDetails {

    public SecUser(UserEntity user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.enabled = user.getActive();
        this.authorities = user.getRoles().stream()
                .flatMap(role -> {
                    List<SimpleGrantedAuthority> combined = new ArrayList<>();

                    combined.add(new SimpleGrantedAuthority(role.getRole()));

                    for (AuthorityEntity authority : role.getAuthorities()) {
                        combined.add(new SimpleGrantedAuthority(authority.getAuthority()));
                    }

                    return combined.stream();
                })
                .collect(Collectors.toList());
        this.user = user;
    }

    private final String username;
    private final String password;
    private final boolean enabled;
    private final Collection<SimpleGrantedAuthority> authorities;
    @Getter
    private final UserEntity user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
