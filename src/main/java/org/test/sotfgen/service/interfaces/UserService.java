package org.test.sotfgen.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.test.sotfgen.dto.ChangePasswordDto;
import org.test.sotfgen.dto.Password;
import org.test.sotfgen.dto.UserDto;
import org.test.sotfgen.entity.UserEntity;

@Service
public interface UserService {


    Page<UserEntity> getUsers(Pageable pageable);

    UserEntity getUser(Integer id);

    UserEntity createUser(UserDto user);

    UserEntity updateEMail(UserDto user, Integer id);

    void deleteUser(Integer id);

    void deactivateUser(Password password);

    void resetPass();

    void resetPassAdmin(String username);

    void changePass(ChangePasswordDto changePasswordDto, String jwtToken);
}
