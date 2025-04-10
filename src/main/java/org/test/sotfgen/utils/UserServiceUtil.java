package org.test.sotfgen.utils;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.test.sotfgen.config.SecUser;
import org.test.sotfgen.entity.UserEntity;

import java.util.Collection;


public class UserServiceUtil {

    public static boolean activeUserHasAuthority(String permission) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        return authorities.stream()
                .anyMatch(a -> a.getAuthority().equals(permission));
    }

    public static UserEntity getActiveUser() throws AuthenticationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof SecUser) {
            SecUser userDetails = (SecUser) authentication.getPrincipal();

            return userDetails.getUser();
        }

        throw new AuthenticationException("unauthenticated");
    }
}
