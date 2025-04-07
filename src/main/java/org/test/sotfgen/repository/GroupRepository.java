package org.test.sotfgen.repository;

import org.springframework.stereotype.Repository;
import org.test.sotfgen.entity.GroupEntity;

import java.util.Optional;

@Repository
public interface GroupRepository extends BasicRepository<GroupEntity, Long> {
    Optional<GroupEntity> findById(Integer id);
}
