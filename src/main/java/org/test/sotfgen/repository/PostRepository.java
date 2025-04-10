package org.test.sotfgen.repository;

import org.springframework.stereotype.Repository;
import org.test.sotfgen.entity.PostEntity;

@Repository
public interface PostRepository extends BasicRepository<PostEntity, Integer> {
}
