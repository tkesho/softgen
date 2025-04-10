package org.test.sotfgen.repository;

import org.springframework.stereotype.Repository;
import org.test.sotfgen.entity.FileEntity;

@Repository
public interface FileRepository extends BasicRepository<FileEntity, Integer> {
}
