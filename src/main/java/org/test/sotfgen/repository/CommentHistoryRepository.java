package org.test.sotfgen.repository;

import org.springframework.stereotype.Repository;
import org.test.sotfgen.entity.CommentHistoryEntity;

@Repository
public interface CommentHistoryRepository extends BasicRepository<CommentHistoryEntity, Integer> {
}
