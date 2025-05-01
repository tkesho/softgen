package org.test.sotfgen.repository;

import org.springframework.stereotype.Repository;
import org.test.sotfgen.entity.HttpRequestEntity;

@Repository
public interface LoggingRepository extends BasicRepository<HttpRequestEntity, Integer> {

}
