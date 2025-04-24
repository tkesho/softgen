package org.test.sotfgen.repository;

import org.springframework.stereotype.Repository;
import org.test.sotfgen.entity.AuthorityEntity;

@Repository
public interface AuthorityRepository extends BasicRepository<AuthorityEntity, Integer> {
}
