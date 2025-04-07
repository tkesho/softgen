package org.test.sotfgen.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.test.sotfgen.dto.UserEntityDto;
import org.test.sotfgen.entity.UserEntity;

@Service
public interface UserService {
    Page<UserEntity> getUsers(Pageable pageable);
    UserEntity getUser(Integer id);
    UserEntity createUser(UserEntityDto user);
    UserEntity updateUser(UserEntityDto user, Integer id);
    void deleteUser(Integer id);
    UserEntity getUserById(Integer userId);
}
