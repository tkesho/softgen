package org.test.sotfgen.repository;

import org.springframework.stereotype.Repository;
import org.test.sotfgen.entity.PersonDetailEntity;

import java.util.Optional;

@Repository
public interface PersonDetailRepository extends BasicRepository<PersonDetailEntity, Integer>{
    Optional<PersonDetailEntity> findByUserId(Integer id);
}
