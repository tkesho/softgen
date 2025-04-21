package org.test.sotfgen.security;

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
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.enabled = user.getActive();
        this.authorities = user.getRoles().stream()
                .flatMap(role -> {
                    List<SimpleGrantedAuthority> combined = new ArrayList<>();

                    combined.add(new SimpleGrantedAuthority(role.getName()));

                    for (AuthorityEntity authority : role.getAuthorities()) {
                        combined.add(new SimpleGrantedAuthority(authority.getName()));
                    }

                    return combined.stream();
                })
                .collect(Collectors.toList());
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
