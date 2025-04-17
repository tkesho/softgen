package org.test.sotfgen.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.test.sotfgen.config.SecUser;
import org.test.sotfgen.dto.Password;
import org.test.sotfgen.dto.UserDto;
import org.test.sotfgen.entity.UserEntity;

@Service
public interface UserService {


    Page<UserEntity> getUsers(Pageable pageable);

    UserEntity getUser(Integer id);

    UserEntity createUser(UserDto user);

    UserEntity updateEMail(SecUser secUser, UserDto user, Integer id);

    void deleteUser(Integer id);

    void deactivateUser(SecUser secUser, Password password);

    void resetPass(SecUser secUser);

    void resetPassAdmin(SecUser secUser, String username);

    void changePass(SecUser secUser, String oldPassword, Password newPassword);
}
