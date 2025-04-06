package org.test.sotfgen.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.test.sotfgen.Exceptions.user.UserNotFoundException;
import org.test.sotfgen.dto.UserEntityDto;
import org.test.sotfgen.entity.UserEntity;
import org.test.sotfgen.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Page<UserEntity> getUsers(PageRequest pageRequest) {
        return userRepository.findByActive(true, pageRequest);
    }

    @Override
    public UserEntity getUser(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("user with id " + id + " not found"));
    }

    @Override
    @Transactional
    public UserEntity createUser(UserEntityDto user) {
        UserEntity userToCreate = new UserEntity(user);
        userToCreate.setPassword(new BCryptPasswordEncoder().encode(userToCreate.getPassword()));
        return userRepository.save(userToCreate);
    }

    @Override
    @Transactional
    public UserEntity updateUser(UserEntityDto user, Integer id) {
        UserEntity userToUpdate = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("user with id " + id + " not found"));
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setEmail(user.getEmail());
        return userRepository.save(userToUpdate);
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        UserEntity userToDelete = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(("user with id " + id + " not found")));
        userToDelete.setActive(false);
        userRepository.save(userToDelete);
    }
}
