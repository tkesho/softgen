package org.test.sotfgen.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.test.sotfgen.entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends BasicRepository<UserEntity, Integer> {
    Page<UserEntity> findByActive(Boolean active, Pageable pageable);
    Optional<UserEntity> findByUsername(String username);
}
