package org.test.sotfgen.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.test.sotfgen.Exceptions.user.UserNotFoundException;
import org.test.sotfgen.entity.UserEntity;
import org.test.sotfgen.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserServiceUtil {

    private final UserRepository userRepository;

    public UserEntity getUserById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(("user with id " + userId + " not found")));
    }

    public boolean userHasAuthority(Integer userId, String permission) {
        UserEntity user = getUserById(userId);
        return user.getRoles().stream()
                .flatMap(role -> role.getAuthorities().stream())
                .anyMatch(a -> a.getName().equals(permission));
    }

    public boolean userHasRole(Integer userId, String role) {
        UserEntity user = getUserById(userId);
        return user.getRoles().stream()
                .anyMatch(r -> r.getName().equals(role));
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