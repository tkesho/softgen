package org.test.sotfgen.repository;

import org.springframework.stereotype.Repository;
import org.test.sotfgen.entity.AuthorityEntity;

@Repository
public interface authorityRepository extends BasicRepository<AuthorityEntity, Integer> {
}
