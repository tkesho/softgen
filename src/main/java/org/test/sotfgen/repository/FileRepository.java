package org.test.sotfgen.repository;

import org.springframework.stereotype.Repository;
import org.test.sotfgen.entity.FileEntity;

import java.util.Optional;

@Repository
public interface FileRepository extends BasicRepository<FileEntity, Integer> {
    Optional<FileEntity> findByObjectKey(String objectKey);
}
