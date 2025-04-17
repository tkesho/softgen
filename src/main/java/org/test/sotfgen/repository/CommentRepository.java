package org.test.sotfgen.repository;

import org.springframework.stereotype.Repository;
import org.test.sotfgen.entity.CommentEntity;

@Repository
public interface CommentRepository extends BasicRepository<CommentEntity, Integer> {
}
