package org.test.sotfgen.repository;

import org.springframework.stereotype.Repository;
import org.test.sotfgen.entity.EmailEntity;

@Repository
public interface EmailRepository extends BasicRepository<EmailEntity, Integer> {
}
