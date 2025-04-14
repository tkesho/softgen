package org.test.sotfgen.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.test.sotfgen.Exceptions.IncorrectPasswordException;
import org.test.sotfgen.config.SecUser;
import org.test.sotfgen.dto.Password;
import org.test.sotfgen.dto.UserDto;
import org.test.sotfgen.entity.UserEntity;
import org.test.sotfgen.mapper.UserMapper;
import org.test.sotfgen.repository.UserRepository;
import org.test.sotfgen.utils.UserServiceUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserServiceUtil userServiceUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserEntity> getUsers(Pageable pageable) {
        return userRepository.findByActive(true, pageable);
    }

    @Override
    public UserEntity getUser(Integer id) {
        return userRepository.save(userServiceUtil.getUserById(id));
    }

    @Override
    @Transactional
    public UserEntity createUser(UserDto user) {
        UserEntity userToCreate = userMapper.userDtoToUser(user);
        userToCreate.setPassword(passwordEncoder.encode(userToCreate.getPassword()));
        return userRepository.save(userToCreate);
    }

    @Override
    @Transactional
    public UserEntity updateEMail(SecUser secUser, UserDto user, Integer id) {
        UserEntity userToUpdate = userServiceUtil.getUserById(id);
        if (user.getPassword() != null) {
            userToUpdate.setEmail(user.getEmail());
        }
        return userRepository.save(userToUpdate);
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        UserEntity userToDelete = userServiceUtil.getUserById(id);
        userToDelete.setActive(false);
        userRepository.save(userToDelete);
    }

    @Override
    @Transactional
    public void deactivateUser(SecUser secUser, Password password) {
        if(!passwordEncoder.matches(password.getPassword(), secUser.getPassword())) {
            throw new IncorrectPasswordException("Password not correct");
        }
        UserEntity userToDeactivate = userServiceUtil.getUserById(secUser.getId());
        userToDeactivate.setActive(false);
        userRepository.save(userToDeactivate);
    }

    @Override
    @Transactional
    public String resetPass(SecUser secUser) {
        UserEntity user = userServiceUtil.getUserById(secUser.getId());
        String newPassword = userServiceUtil.generateRandomPassword(12);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return newPassword;
    }

    @Override
    public String resetPassAdmin(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        String newPassword = userServiceUtil.generateRandomPassword(12);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return newPassword;
    }

    @Override
    @Transactional
    public void changePass(SecUser secUser, String oldPassword, Password newPassword) {
        if (!passwordEncoder.matches(oldPassword, secUser.getPassword())) {
            throw new IncorrectPasswordException("Old password is incorrect");
        }
        UserEntity user = userServiceUtil.getUserById(secUser.getId());
        user.setPassword(passwordEncoder.encode(newPassword.getPassword()));
        userRepository.save(user);
    }
}
