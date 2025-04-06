//package org.test.sotfgen.config;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.test.sotfgen.entity.UserEntity;
//
//import java.util.Collection;
//
//public class SecUser implements UserDetails {
//
//    public SecUser(UserEntity user) {
//        this.username = user.getUsername();
//        this.password = user.getPassword();
//        this.enabled = user.isActive();
//        this.id = user.getId();
//    }
//
//    private final String username;
//    private final String password;
//    private final boolean enabled;
//    private final Integer id;
//    private  Collection<SimpleGrantedAuthority> authorities;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return enabled;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return enabled;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return enabled;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return enabled;
//    }
//}
