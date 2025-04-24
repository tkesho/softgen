package org.test.sotfgen.service.classes;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.test.sotfgen.dto.ChangePasswordDto;
import org.test.sotfgen.dto.Password;
import org.test.sotfgen.dto.UserDto;
import org.test.sotfgen.entity.PersonEntity;
import org.test.sotfgen.entity.UserEntity;
import org.test.sotfgen.exceptions.IncorrectPasswordException;
import org.test.sotfgen.mapper.PersonMapper;
import org.test.sotfgen.mapper.UserMapper;
import org.test.sotfgen.repository.UserRepository;
import org.test.sotfgen.service.interfaces.PersonService;
import org.test.sotfgen.service.interfaces.UserService;
import org.test.sotfgen.utils.TokenUtil;
import org.test.sotfgen.utils.UserUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserUtil userUtil;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;
    private final TokenUtil tokenUtil;
    private final PersonService personService;
    private final PersonMapper personMapper;

    @Override
    public Page<UserEntity> getUsers(Pageable pageable) {
        return userRepository.findByActive(true, pageable);
    }

    @Override
    public UserEntity getUser(Integer id) {
        return userRepository.save(userUtil.getUserById(id));
    }

    @Override
    @Transactional
    public UserEntity createUser(UserDto user) {
        UserEntity userToCreate = userMapper.userDtoToUser(user);
        userToCreate.setPassword(passwordEncoder.encode(userToCreate.getPassword()));
        UserEntity userToSave = userRepository.save(userToCreate);
        personService.createPerson(personMapper.personDetailToPersonDetailDto(new PersonEntity()), userToSave.getId());
        return userToSave;
    }

    @Override
    @Transactional
    public UserEntity updateEMail(UserDto user, Integer id) {
        UserEntity secUser = userUtil.getActingPrincipal();
        UserEntity userToUpdate = userUtil.getUserById(id);
        if (user.getPassword() != null) {
            userToUpdate.setEmail(user.getEmail());
        }
        return userRepository.save(userToUpdate);
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        UserEntity userToDelete = userUtil.getUserById(id);
        userToDelete.setActive(false);
        userRepository.save(userToDelete);
    }

    @Override
    @Transactional
    public void deactivateUser(Password password) {
        UserEntity user = userUtil.getActingPrincipal();

        if (!passwordEncoder.matches(password.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Password not correct");
        }

        UserEntity userToDeactivate = userUtil.getUserById(user.getId());
        userToDeactivate.setActive(false);

        String message = String.format("Hello %s, Your account has been deactivated!", userToDeactivate.getUsername());
        emailSenderService.sendEmail(userToDeactivate.getEmail(), "Account deactivation", message);

        tokenUtil.deactivateToken(userToDeactivate.getUsername());
        userRepository.save(userToDeactivate);
    }

    @Transactional
    public void resetPass() {
        UserEntity user = userUtil.getActingPrincipal();

        String newPassword = userUtil.generateRandomPassword(12);
        user.setPassword(passwordEncoder.encode(newPassword));

        tokenUtil.deactivateToken(user.getUsername());
        userRepository.save(user);

        String message = String.format("Hello %s, Your new password has been set to: %s !", user.getUsername(), newPassword);
        emailSenderService.sendEmail(user.getEmail(), "Password reset", message);
    }

    @Override
    public void resetPassAdmin(String username) {
        UserEntity secUser = userUtil.getActingPrincipal();
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));

        String newPassword = userUtil.generateRandomPassword(12);
        user.setPassword(passwordEncoder.encode(newPassword));

        String message = String.format("Hello %s, user with name %s has reset you password. New password is: %s !", username, userUtil.getUserById(secUser.getId()).getUsername(), newPassword);
        emailSenderService.sendEmail(user.getEmail(), "Password reset by admin user", message);

        tokenUtil.deactivateToken(user.getUsername());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePass(ChangePasswordDto changePasswordDto, String jwtToken) {

        UserEntity secUser = userUtil.getActingPrincipal();

        if (!passwordEncoder.matches(changePasswordDto.oldPassword(), secUser.getPassword())) {
            throw new IncorrectPasswordException("Old password is incorrect");
        }

        UserEntity user = userUtil.getUserById(secUser.getId());
        user.setPassword(passwordEncoder.encode(changePasswordDto.newPassword()));

        String message = String.format("Hello %s, Your password has been changed!", user.getUsername());
        emailSenderService.sendEmail(user.getEmail(), "Password change", message);

        tokenUtil.deactivateToken(secUser.getUsername());
        userRepository.save(user);
    }
}