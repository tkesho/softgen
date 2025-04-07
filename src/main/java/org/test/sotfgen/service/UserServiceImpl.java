package org.test.sotfgen.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.test.sotfgen.Exceptions.user.UserNotFoundException;
import org.test.sotfgen.dto.UserEntityDto;
import org.test.sotfgen.entity.UserEntity;
import org.test.sotfgen.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Page<UserEntity> getUsers(Pageable pageable) {
        Page<UserEntity> page = userRepository.findByActive(true, pageable);
        List<UserEntity> filtered = page.getContent().stream()
                .peek(userEntity -> userEntity.setPassword(null))
                .toList();
        return new PageImpl<>(filtered, pageable, page.getTotalElements());
    }

    @Override
    public UserEntity getUser(Integer id) {
        UserEntity usr = userRepository.save(getUserById(id));
        usr.setId(null);
        return usr;
    }

    @Override
    @Transactional
    public UserEntity createUser(UserEntityDto user) {
        UserEntity userToCreate = new UserEntity(user);
        userToCreate.setPassword(new BCryptPasswordEncoder().encode(userToCreate.getPassword()));
        UserEntity usr = userRepository.save(userToCreate);
        usr.setId(null);
        return usr;
    }

    @Override
    @Transactional
    public UserEntity updateUser(UserEntityDto user, Integer id) {
        UserEntity userToUpdate = getUserById(id);
        if(user.getUsername() != null) {
            userToUpdate.setUsername(user.getUsername());
        }
        if (user.getPassword() != null) {
            userToUpdate.setEmail(user.getEmail());
        }
        UserEntity usr = userRepository.save(userToUpdate);
        usr.setId(null);
        return usr;
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        UserEntity userToDelete = getUserById(id);
        userToDelete.setActive(false);
        userRepository.save(userToDelete);
    }

    public UserEntity getUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(("user with id " + id + " not found")));
    }
}
