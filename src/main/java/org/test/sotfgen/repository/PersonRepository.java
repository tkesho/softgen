package org.test.sotfgen.repository;

import org.springframework.stereotype.Repository;
import org.test.sotfgen.entity.PersonEntity;

import java.util.Optional;

@Repository
public interface PersonRepository extends BasicRepository<PersonEntity, Integer>{
    Optional<PersonEntity> findByUserId(Integer id);
}
