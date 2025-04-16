package org.test.sotfgen.repository;

import org.springframework.stereotype.Repository;
import org.test.sotfgen.entity.GroupEntity;

@Repository
public interface GroupRepository extends BasicRepository<GroupEntity, Integer> {
}
