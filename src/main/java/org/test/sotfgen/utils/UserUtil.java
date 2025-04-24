package org.test.sotfgen.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.test.sotfgen.exceptions.UserNotFoundException;
import org.test.sotfgen.entity.UserEntity;
import org.test.sotfgen.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserUtil {

    private final UserRepository userRepository;

    public UserEntity getUserById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(("user with id " + userId + " not found")));
    }

    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(("user with email " + email + " not found")));
    }

    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(("user with username " + username + " not found")));
    }

    public UserEntity getActingPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(("user with username " + username + " not found")));
    }

    public boolean userHasAuthority(Integer userId, String permission) {
        UserEntity user = getUserById(userId);
        return user.getAuthorities().stream().anyMatch(a -> a.getName().equals(permission));
    }

    public String generateRandomPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }
}