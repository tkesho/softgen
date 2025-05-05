package org.test.sotfgen.repository;

import org.springframework.stereotype.Repository;
import org.test.sotfgen.entity.TeamEntity;

@Repository
public interface TeamRepository extends BasicRepository<TeamEntity, Integer> {
}
