package org.test.sotfgen.repository;

import org.springframework.stereotype.Repository;
import org.test.sotfgen.entity.PostHistoryEntity;

@Repository
public interface PostHistoryRepository extends BasicRepository<PostHistoryEntity, Integer> {
}
